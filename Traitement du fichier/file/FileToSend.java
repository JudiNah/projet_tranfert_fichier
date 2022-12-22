package file;

public class FileToSend {
     private int id;
     private String name;
     private byte[] data;
     private String fileExtension;

     public void setId(int id) {
          this.id = id;
     }
     public void setName(String name) {
          this.name = name;
     }
     public void setData(byte[] data) {
          this.data = data;
     }
     public void setFileExtension(String fileExtension) {
          this.fileExtension = fileExtension;
     }
     public int getId() {
          return id;
     }
     public String getName() {
          return name;
     }
     public byte[] getData() {
          return data;
     }
     public String getFileExtension() {
          return fileExtension;
     }

     public FileToSend(int id,String name,byte[] data,String fileExtension) {
          setId(id);
          setName(name);
          setData(data);
          setFileExtension(fileExtension);
     }
}
