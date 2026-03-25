package poiw.project.CAS;

public class elsData {
	private String name;
	private String surname;
	private  String albumNumber;

	public elsData(String name, String surname, String albumNumber) {
		this.name = name;
		this.surname = surname;
		this.albumNumber = albumNumber;
	}

	public String getName() { return name; }
	public String getSurname() { return surname; }
	public String getAlbumNumber() { return albumNumber; }
}
