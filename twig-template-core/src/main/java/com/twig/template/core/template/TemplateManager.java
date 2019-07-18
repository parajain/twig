package com.twig.template.core.template;

import static com.twig.template.core.template.TemplateConstants.FIN_TEMPLATE_FILE;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.twig.template.core.exceptions.TwigTemplateException;
import com.twig.template.core.template.pojo.Parameter;
import com.twig.template.core.template.pojo.TemplateDefination;
import com.twig.template.core.template.pojo.TemplateMappings;
import com.twig.template.core.template.pojo.TemplateResponse;
import com.twig.template.core.util.FileProcess;

public class TemplateManager {

	static private HashMap<String, TemplateDefination> mappings;
	Handlebars handlebars;

	public TemplateManager(String templateDefinations) {
		load(templateDefinations);
		handlebars = new Handlebars();
		registerHelpers();
	}

	public TemplateManager() {
		String templateDefinations = FileProcess.readFile(FIN_TEMPLATE_FILE);
		load(templateDefinations);
		handlebars = new Handlebars();
		registerHelpers();
	}

	
	public boolean load(String templateDefinations) {

		ObjectMapper mapper = new ObjectMapper();
		TemplateMappings jsonList = null;

		try {
			// templateFile = new File(templateFile).getAbsolutePath();
			// System.out.println(templateFile);
			// Convert JSON string from file to Object
			//jsonList = mapper.readValue(templateFile, TemplateMappings.class);
			jsonList = mapper.readValue(templateDefinations, TemplateMappings.class);
			// Pretty print
			// String prettyJsonList =
			// mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonList);
			// System.out.println(prettyJsonList);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// create a mappings hashmap with queryID as key
		mappings = new HashMap<String, TemplateDefination>();
		for (TemplateDefination templateDefination : jsonList.getAllMappings()) {
			TemplateDefination existingMapping = mappings.get(templateDefination.getTemplateID());
			if (existingMapping != null) {
				System.err.println("Duplicate template for query goal:" + templateDefination.getTemplateID());
				return false;
			} else {
				mappings.put(templateDefination.getTemplateID(), templateDefination);
			}
		}

		return true;
	}

	public TemplateDefination getMapping(String templateId) {
		return mappings.get(templateId);
	}

	public List<Parameter> getTemplateParameters(String templateId) {
		TemplateDefination templateDefination = mappings.get(templateId);
		if (null == templateDefination) {
			throw new IllegalArgumentException("Template with " + templateId + " is not present");
		}
		return templateDefination.getTemplateParameters();
	}

	public List<Parameter> getHeadTemplateParameters(String templateId) {
		TemplateDefination templateDefination = mappings.get(templateId);
		if (null == templateDefination) {
			throw new IllegalArgumentException("Template with " + templateId + " is not present");
		}
		return templateDefination.getHeadTemplateParameters();
	}

	public List<Parameter> getFootTemplateParameters(String templateId) {
		TemplateDefination templateDefination = mappings.get(templateId);
		if (null == templateDefination) {
			throw new IllegalArgumentException("Template with " + templateId + " is not present");
		}
		return templateDefination.getFootTemplateParameters();
	}

	public List<String> getFootNotes(String templateId) {
		return mappings.get(templateId).getFootNoteId();
	}

	public TemplateResponse fillTemplate(String templateId, HashMap<String, Object> templateParameter,
			HashMap<String, Object> headTemplateParameter, HashMap<String, Object> footTemplateParameter)
			throws IOException, TwigTemplateException {
		// ToDo(parag): Move handlebar compiled templates to a compiled list,
		// compiling template takes time. Should be done once.

		String templateNlg = null, headNlg = null, footNlg = null;

		if (!mappings.containsKey(templateId)) {
			throw new TwigTemplateException("No mappings found for template id: " + templateId);
		}

		List<String> templateStrings = mappings.get(templateId).getTemplateStrings();
		List<String> headTemplateStrings = mappings.get(templateId).getHeadTemplates();
		List<String> footTemplateStrings = mappings.get(templateId).getFootTemplates();

		if (null != templateParameter) {
			if (null != templateStrings && templateStrings.size() > 0) {
				String templateString = TemplatePicker.pickTemplate(templateStrings);
				Template template = handlebars.compileInline(templateString);
				templateNlg = template.apply(templateParameter);
			}
		}

		if (null != headTemplateParameter) {
			if (null != templateStrings && templateStrings.size() > 0) {
				String headTemplateString = TemplatePicker.pickTemplate(headTemplateStrings);
				Template template = handlebars.compileInline(headTemplateString);
				headNlg = template.apply(templateParameter);
			}
		}
		if (null != footTemplateParameter) {
			if (null != footTemplateStrings && footTemplateParameter.size() > 0) {
				String footTemplateString = TemplatePicker.pickTemplate(footTemplateStrings);

				Template template = handlebars.compileInline(footTemplateString);
				footNlg = template.apply(templateParameter);
			}
		}
		List<String> footNoteIds = mappings.get(templateId).getFootNoteId();

		TemplateResponse response = new TemplateResponse();
		response.setTemplateNlg(templateNlg);
		response.setHeadNlg(headNlg);
		response.setFootNlg(footNlg);
		response.setFootNoteIds(footNoteIds);

		return response;
	}

	private void registerHelpers() {
		handlebars.registerHelper("iff", new Helper<Object>() {
			public CharSequence apply(final Object context, final Options options) throws IOException {
				String a = context.toString();
				if (options.params.length > 0) {
					String b = options.param(0);

					if (a.equals(b)) {
						return options.fn().toString();
					} else {
						return options.inverse().toString();
					}
				} else {
					throw new IOException("iff expects atleast one option parameter to compare with.");
				}

			}
		});
		
		handlebars.registerHelper("if_gt", new Helper<Object>() {
			public CharSequence apply(final Object context, final Options options) throws IOException {
				Double a = Double.parseDouble(context.toString());
				if (options.params.length > 0) {
					String bstr = options.param(0);
					Double b = Double.parseDouble(bstr);
					if (a > b) {
						return options.fn().toString();
					} else {
						return options.inverse().toString();
					}
				} else {
					throw new IOException("if_gt expects atleast one option parameter to compare with.");
				}
			}
		});
		
		handlebars.registerHelper("if_leq", new Helper<Object>() {
			public CharSequence apply(final Object context, final Options options) throws IOException {
				Double a = Double.parseDouble(context.toString());
				if (options.params.length > 0) {
					String bstr = options.param(0);
					Double b = Double.parseDouble(bstr);
					//System.out.println("************" + b + " " + options.inverse().toString());
					if (a <= b) {
						return options.fn().toString();
					} else {
						return options.inverse().toString();
					}
				} else {
					throw new IOException("if_leq expects atleast one option parameter to compare with.");
				}
			}
		});
	}

	public static void main(String args[]) {
		// TemplateManager templateManager = new TemplateManager();
		// templateManager.load();
	}
}
