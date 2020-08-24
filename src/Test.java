import org.jboss.dna.common.text.Inflector;

public class Test {

	public static void main(String[] args) {
		System.out.println(Inflector.getInstance().pluralize("person", 1));
		System.out.println(Inflector.getInstance().pluralize("person", 10));
	}

}
