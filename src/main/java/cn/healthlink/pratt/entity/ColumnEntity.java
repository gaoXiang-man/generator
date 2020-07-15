
package cn.healthlink.pratt.entity;


import java.util.Objects;

/**
 * 列的属性
 *
 */
public class ColumnEntity {
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
		this.setColumnNameL(columnName);
	}

	public String getColumnNameL() {
		return columnNameL;
	}

	public String getAttrTypeL() {
		return attrTypeL;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;

	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrname() {
		return attrname;
	}

	public void setAttrname(String attrname) {
		this.attrname = attrname;
	}

	public String getAttrType() {
		return attrType;
	}

	public void setAttrType(String attrType) {
		this.attrType = attrType;
		setAttrTypeL(attrType);
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	//列名
    private String columnName;

	public void setColumnNameL(String columnNameL) {
		if (Objects.nonNull(columnNameL)){
			this.columnNameL = columnNameL.toLowerCase();
		}
	}

	//列名全部小写
	private String columnNameL;

	public void setAttrTypeL(String attrTypeL) {
		if (Objects.nonNull(attrTypeL)) {
			this.attrTypeL = attrTypeL.toLowerCase();
		}
	}

	//属性类型全部小写
	private String attrTypeL;

    //列名类型
    private String dataType;
    //列名备注
    private String comments;


	//属性名称(第一个字母大写)，如：user_name => UserName
    private String attrName;
    //属性名称(第一个字母小写)，如：user_name => userName
    private String attrname;
    //属性类型
    private String attrType;
    //auto_increment
    private String extra;


}
