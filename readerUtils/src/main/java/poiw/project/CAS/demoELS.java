package poiw.project.CAS;

public class demoELS {
	public static void main(String[] args) {
		Reader reader = new Reader();
		reader.initialize();
		System.out.println(reader.getReaders());
		reader.setReader(0);
		System.out.println(reader.getElsData().toString());
	}
}
