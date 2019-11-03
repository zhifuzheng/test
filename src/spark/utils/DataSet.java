package spark.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
 /**
  * 读取excel文件的实体类
  * @author yangLee
  *
  */
public class DataSet {
    private String[] headers;
    private String[] rowHeaders;
    private List<String[]> datasList = new ArrayList<String[]>();
    private Set<String> conStrctSet;
 
    public DataSet(String[] headers, String[] rowHeaders,
            List<String[]> datasList, Set<String> conStrctSet) {
        this.headers = headers;
        this.rowHeaders = rowHeaders;
        this.datasList = datasList;
        this.conStrctSet = conStrctSet;
    }
 
    public DataSet(String[] header, String[] rowsHeader,
            List<String[]> datasList2) {
        this.headers = header;
        this.rowHeaders = rowsHeader;
        this.datasList = datasList2;
    }
 
    public String[] getHeaders() {
        return headers;
    }
 
    public void setHeaders(String[] headers) {
        this.headers = headers;
    }
 
    public String[] getRowHeaders() {
        return rowHeaders;
    }
 
    public void setRowHeaders(String[] rowHeaders) {
        this.rowHeaders = rowHeaders;
    }
 
    public List<String[]> getDatasList() {
        return datasList;
    }
 
    public void setDatasList(List<String[]> datasList) {
        this.datasList = datasList;
    }
 
    public Set<String> getConStrctSet() {
        return conStrctSet;
    }
 
    public void setConStrctSet(Set<String> conStrctSet) {
        this.conStrctSet = conStrctSet;
    }
}