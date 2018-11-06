package xiao_tool;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体类的resultMap映射
 */
public class ResultMapGenerator {
    private Class clazz;
    private static final Document doc = DocumentHelper.createDocument();
    /**
     * 常见字段类型数组
     */
    private static final String[] FIELD_TYPE = new String[]{
            "int", "short", "byte", "long", "float",
            "double", "boolean", "Integer", "Short",
            "Byte", "Long", "Float", "Double", "Boolean",
            "Object", "B", "Date", "String", "BigDecimal"};

    /**
     * 与字段类型对应的jdbcType数组
     */
    private static final String[] JDBC_TYPE = new String[]{
            "INTEGER", "SMALLINT", "TINYINT", "BIGINT",
            "REAL", "DOUBLE", "VARCHAR", "INTEGER", "SMALLINT",
            "TINYINT", "BIGINT", "REAL", "DOUBLE", "VARCHAR",
            "OTHER", "BINARY", "TIMESTAMP", "VARCHAR", "DECIMAL"};
    private List<String[]> list;

    public ResultMapGenerator(Class clazz) {
        this.clazz = clazz;
        this.list = this.resultList();
    }

    /**
     * 生成xml文件方法
     * 包含resultMap
     */
    public void createXml() {
        doc.addComment("由实体类" + clazz.getName() + "生成mybatis对应的resultMap");
        Element root = this.createRootElement();
        this.createResultElement(root);
        this.writeToXml();
    }

    private void writeToXml() {
        String path = "ResultMapGenerator.xml";
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        try {
            Writer out = new FileWriter(path);
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(doc);
            writer.close();
            System.out.print("生成ResultMapGenerator.XML文件成功");
        } catch (IOException var4) {
            System.out.print("生成ResultMapGenerator.XML文件失败");
            var4.printStackTrace();
        }

    }

    private void createResultElement(Element root) {
        if (this.list != null && this.list.size() > 0) {
            for (int i = 0; i < this.list.size(); ++i) {
                Element result = root.addElement("result");
                result.addAttribute("column", ((String[]) this.list.get(i))[0]);
                result.addAttribute("jdbcType", ((String[]) this.list.get(i))[1]);
                result.addAttribute("property", ((String[]) this.list.get(i))[2]);
            }
        }

    }

    private String getJdbcType(String key) {
        key = this.getType(key);
        String jdbcType = "VARCHAR";

        for (int i = 0; i < FIELD_TYPE.length; ++i) {
            if (key.equalsIgnoreCase(FIELD_TYPE[i])) {
                jdbcType = JDBC_TYPE[i];
                return jdbcType;
            }
        }

        return jdbcType;
    }

    private Element createRootElement() {
        Element root = doc.addElement("resultMap");
        root.addAttribute("id", this.clazz.getSimpleName() + "ResultMap");
        root.addAttribute("type", this.clazz.getName());
        return root;
    }

    private ArrayList<String[]> resultList() {
        String column;
        String jdbcType;
        ArrayList<String[]> list = new ArrayList();
        Field[] fields = this.clazz.getDeclaredFields();
        Field[] var7 = fields;
        int var8 = fields.length;

        for (int var9 = 0; var9 < var8; ++var9) {
            Field field = var7[var9];
            String[] result = new String[3];
            String property = field.getName();
            result[2] = property;
            String fieldType = field.getGenericType().toString();
            column = this.getColumn(property);
            result[0] = column;
            jdbcType = this.getJdbcType(fieldType);
            result[1] = jdbcType;
            list.add(result);
        }
        return list;
    }

    private String getColumn(String name) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);
            if (i != 0 && 'A' <= ch && ch <= 'Z') {
                ch = (char) (ch + 32);
                sb.append("_" + ch);
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    private String getType(String type) {
        String tp = new String();
        if ("class [B".equals(type)) {
            tp = "B";
            return tp;
        } else {
            String[] split = type.split("\\.");
            if (split.length != 0) {
                tp = split[split.length - 1];
            }
            return tp;
        }
    }
}
