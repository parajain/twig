import com.twig.template.core.exceptions.TwigTemplateException;
import com.twig.template.core.processor.InputProcessor;
import com.twig.template.core.processor.TwigTemplateDriver;
import com.twig.template.core.util.FileProcess;


// Add the compiled jar of twig template core from resources to your project. Or add in the class path before running this example.
public class Test {

	private static final String TEST_TEMPLATE_FILE = "resources/testfiles/test-templates.json";

	public static void main(String[] args) {
		String jsonInput = FileProcess.readFile("resources/testfiles/discount-template-input.json");
		String tempDef = FileProcess.readFile(TEST_TEMPLATE_FILE);
		TwigTemplateDriver driver = new TwigTemplateDriver(tempDef);
		String o;
		try {
			o = driver.getNLGFromJsonString(jsonInput);
			System.out.println(o);
		} catch (TwigTemplateException e) {
			e.printStackTrace();
		}
	}

}
