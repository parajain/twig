package com.twig.template.core.processor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twig.template.core.exceptions.TwigTemplateException;
import com.twig.template.core.template.TemplateManager;
import com.twig.template.core.template.pojo.Parameter;
import com.twig.template.core.template.pojo.TemplateResponse;
import com.twig.template.core.util.FileProcess;

import static com.twig.template.core.processor.JsonInputConstants.ENGLISH;
import static com.twig.template.core.processor.JsonInputConstants.FOOT_TEMPLATE_PARAMETERS;
import static com.twig.template.core.processor.JsonInputConstants.GERMAN;
import static com.twig.template.core.processor.JsonInputConstants.GERMAN_PREFIX;
import static com.twig.template.core.processor.JsonInputConstants.HEAD_TEMPLATE_PARAMETERS;
import static com.twig.template.core.processor.JsonInputConstants.LANGUAGE;
import static com.twig.template.core.processor.JsonInputConstants.TEMPLATE_ID;
import static com.twig.template.core.processor.JsonInputConstants.TEMPLATE_PARAMETERS;
import static com.twig.template.core.template.TemplateConstants.MAP_TYPE;
import static com.twig.template.core.template.TemplateConstants.MULTI_MAP_TYPE;
import static com.twig.template.core.template.TemplateConstants.SINGLE_VALUE_TYPE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;;

public class InputProcessor {

	private ObjectMapper mapper;
	private TemplateManager templateManager;

	public InputProcessor() {
		mapper = new ObjectMapper();
		templateManager = new TemplateManager();
	}
	
	
	public InputProcessor(String templateDefinations) {
		//basically for unit test
		mapper = new ObjectMapper();
		//String templateString = FileProcess.readFile(templateFile);
		templateManager = new TemplateManager(templateDefinations);
	}

	public String getNlg(String jsonStr) throws TwigTemplateException {
		//System.out.println("Input to template driver: " + jsonStr);
		String nlg = null;
		try {
			JsonNode rootArray = mapper.readTree(jsonStr);
			String templateId = getTemplateId(rootArray);

			List<Parameter> templateParameters = templateManager.getTemplateParameters(templateId);
			List<Parameter> footTemplateParameters = templateManager.getFootTemplateParameters(templateId);
			List<Parameter> headTemplateParameters = templateManager.getHeadTemplateParameters(templateId);
			
			JsonNode templateParameterNode = rootArray.findValue(TEMPLATE_PARAMETERS);
			JsonNode footTemplateParameterNode = rootArray.findValue(FOOT_TEMPLATE_PARAMETERS);
			JsonNode headTemplateParameterNode = rootArray.findValue(HEAD_TEMPLATE_PARAMETERS);
			JsonNode langaugeNode = rootArray.findValue(LANGUAGE);
			
			String language = ""; // default english
			if(langaugeNode != null){
				language = cleanString(langaugeNode.toString());
				//System.out.println("Inside template lib getnlg lang: " + language);
				if(GERMAN.equalsIgnoreCase(language)){
					//language = "";
					//  only german now
					System.out.println("German");
					templateId = GERMAN_PREFIX + templateId;
					//System.out.println(templateId);
				}
			}
			else{
				System.out.println("No language information dafault to English.");
			}
			
			HashMap<String, Object> templateParameterMap = buildParameters(templateParameters, templateParameterNode);
			HashMap<String, Object> foottTemplateParameterMap = buildParameters(footTemplateParameters, footTemplateParameterNode);
			HashMap<String, Object> headTemplateParameterMap = buildParameters(headTemplateParameters, headTemplateParameterNode);

			//System.out.println(templateParameterMap);
			if(templateParameterMap == null){
				throw new TwigTemplateException("Unable to extract template parameters.");
			}
			TemplateResponse resp = templateManager.fillTemplate(templateId, templateParameterMap, headTemplateParameterMap, foottTemplateParameterMap);
			String templateNlg = resp.getTemplateNlg();
			String footNlg = resp.getFootNlg();
			String headNlg = resp.getHeadNlg();
			nlg = ((null == headNlg) ? "" : headNlg) + templateNlg + ((null == footNlg) ? "" : footNlg);
			
		} catch (TwigTemplateException te) {
			throw te;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TwigTemplateException(e.getMessage());
		}
		return nlg;
	}

	private HashMap<String, Object> buildParameters(List<Parameter> parameters, JsonNode rootArray)
			throws TwigTemplateException, JsonParseException, JsonMappingException, IOException {
		if(null == rootArray){
			return null;
		}
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		for (Parameter p : parameters) {
			String type = p.getType();
			String name = p.getName();
			boolean required = p.isRequired();
			String defaultValue = p.getDefault_value();
			JsonNode parameterNode = rootArray.findValue(name);

			if (MULTI_MAP_TYPE.equals(type)) {
				if (null != parameterNode) {
					List<Map<String, String>> multiMap = buildMultiMapParameter(parameterNode);
					//System.out.println(multiMap);
					parameterMap.put(name, multiMap);
				} else {
					if (required) {
						throw new TwigTemplateException("Parameter " + name + " of type " + type + " is requied.");
					}
				}
			} else if (MAP_TYPE.equals(type)) {
				if (null != parameterMap) {
					Map<String, String> map = buildMapParameter(parameterNode);
					parameterMap.put(name, map);
				} else {
					if (required) {
						throw new TwigTemplateException("Parameter " + name + " of type " + type + " is requied.");
					}
				}

			} else if (SINGLE_VALUE_TYPE.equals(type)) {
				if (null != parameterNode) {
					String value = getValue(parameterNode);
					parameterMap.put(name, value);
				} else {
					if (required) {
						throw new TwigTemplateException("Parameter " + name + " of type " + type + " is requied.");
					}
				}
			} else {
				throw new TwigTemplateException("Unsupported parameter type " + type);
			}
		}
		return parameterMap;
	}

	private List<Map<String, String>> buildMultiMapParameter(JsonNode parameterNode)
			throws JsonParseException, JsonMappingException, IOException {
		List<Map<String, String>> parameterMapList = mapper.readValue(parameterNode.toString(),
				new TypeReference<List<Map<String, String>>>() {
				});
		return parameterMapList;
	}

	private Map<String, String> buildMapParameter(JsonNode parameterNode)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, String> parameterMap = mapper.readValue(parameterNode.toString(),
				new TypeReference<Map<String, String>>() {
				});
		return parameterMap;
	}

	private String getValue(JsonNode parameterNode) throws JsonParseException, JsonMappingException, IOException {
		String value = cleanString(parameterNode.toString());
		return value;
	}

	private String getTemplateId(JsonNode rootArray) throws TwigTemplateException {
		JsonNode templateNode = rootArray.findValue(TEMPLATE_ID);
		if (null == templateNode || templateNode.toString().isEmpty()) {
			throw new TwigTemplateException("Unable to extract template id from json input");
		}
		return cleanString(templateNode.toString());
	}

	private String cleanString(String s) {
		return s.replaceAll("\"", "");
	}

	public static void main(String args[]) {
		String jsonStr = FileProcess.readFile("resources/testfiles/discount-template-input.json");
		InputProcessor inputProcessor = new InputProcessor();
		try {
			inputProcessor.getNlg(jsonStr);
		} catch (TwigTemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
