package dbms.xml;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dbms.datatypes.DatatypeFactory;
import dbms.exception.DatabaseNotFoundException;
import dbms.exception.TableAlreadyCreatedException;
import dbms.exception.TableNotFoundException;
import dbms.util.Column;
import dbms.util.Table;
import dbms.xml.schema.dtd.DTDSchemaParser;
import dbms.xml.schema.xsd.XSDParser;

public class XMLTableParser implements Parser {
	private static XMLTableParser instance = null;
	private static DocumentBuilder docBuilder = null;
	private static Transformer transformer = null;
	private static final String WORKSPACE_DIR =
			System.getProperty("user.home") + File.separator + "databases";
	private static final ResourceBundle CONSTANTS =
			ResourceBundle.getBundle("dbms.xml.Constants");

	private XMLTableParser() {
		try {
			docBuilder = DocumentBuilderFactory
				.newInstance().newDocumentBuilder();
			transformer = TransformerFactory
					.newInstance().newTransformer();
		} catch (ParserConfigurationException
				| TransformerConfigurationException
				| TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.INDENT,
				"yes");
		transformer.setOutputProperty(
				CONSTANTS.getString("indentation"),
				CONSTANTS.getString("indentation.val"));
	}

	public static XMLTableParser getInstance() {
		if (instance == null) {
			instance = new XMLTableParser();
		}
		return instance;
	}

	@Override
	public void create(Table table) throws DatabaseNotFoundException, TableAlreadyCreatedException {
		File tableFile = new File(openDB(table.getDBName()), table.getName()
				+ CONSTANTS.getString("extension.xml"));
		if (tableFile.exists()) {
			throw new TableAlreadyCreatedException();
		}
		write(table, tableFile);
		XSDParser.getInstance().createSchema(table.getDBName()
				, table.getName());
		DTDSchemaParser.getInstance().createDTDSchema(table.getDBName()
				, table.getName());
	}

	@Override
	public void load(Table table)
			throws TableNotFoundException, DatabaseNotFoundException {
		File tableFile = openTable(table.getDBName(), table.getName());
		Document doc = null;
		try {
			doc = docBuilder.parse(tableFile);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		validateDB(doc, table.getDBName());
		doc.getDocumentElement().normalize();
		parseDataToTable(table, doc);
	}

	@Override
	public void writeTo(Table table) throws TableNotFoundException, DatabaseNotFoundException {
		File tableFile = openTable(table.getDBName(), table.getName());
		write(table, tableFile);
	}

	private void write(Table table, File tableFile) {
		Document doc = docBuilder.newDocument();
		Element tableElement = doc.createElement(
				CONSTANTS.getString("table.element"));
		tableElement.setAttribute(CONSTANTS.getString(
				"name.attr"), table.getName());
		tableElement.setAttribute(CONSTANTS.getString(
				"db.attr"), table.getDBName());
		tableElement.setAttribute(CONSTANTS.getString(
				"rows.attr"), Integer.toString(table.getSize()));
		doc.appendChild(tableElement);
		addColumns(doc, table, tableElement);
		transform(doc, tableFile, table.getName());
	}

	private void addColumns(Document doc, Table table, Element tableElement) {
		for (Column col : table.getColumns()) {
			Element colElement = doc.createElement(
					CONSTANTS.getString("column.element"));
			colElement.setAttribute(
					CONSTANTS.getString("name.attr"), col.getName());
			try {
				colElement.setAttribute(
						CONSTANTS.getString("type.attr"), (String) col
						.getType().getField("KEY").get(col.getType().newInstance()));
			} catch (DOMException | IllegalArgumentException
					| IllegalAccessException | NoSuchFieldException
					| SecurityException | InstantiationException e) {
				e.printStackTrace();
			}
			addRows(doc, col, colElement);
			tableElement.appendChild(colElement);
		}
	}

	private void addRows(Document doc, Column col, Element colElement) {
		int index = 0;
		for (Object entry : col.getEntries()) {
			Element rowElement = doc.createElement(
					CONSTANTS.getString("row.element"));
			rowElement.setAttribute(
					CONSTANTS.getString("index.val"), Integer.toString(index));
			if (entry == null) {
				rowElement.setTextContent("");
			} else {
				rowElement.setTextContent(String.valueOf(entry));
			}
			colElement.appendChild(rowElement);
			index++;
		}
	}

	private void parseDataToTable(Table table, Document doc) {
		int size = Integer.parseInt(doc.getElementsByTagName(
				CONSTANTS.getString("table.element")).item(0)
				.getAttributes().getNamedItem(CONSTANTS.getString(
						"rows.attr")).getTextContent());
		table.setSize(size);
		NodeList colList = doc.getElementsByTagName(
				CONSTANTS.getString("column.element"));
		for (int i = 0; i < colList.getLength(); i++) {
			Node colNode = colList.item(i);
			Column col = new Column();
			String colName = colNode.getAttributes().getNamedItem(
					CONSTANTS.getString("name.attr")).getTextContent();
			String colType = colNode.getAttributes().getNamedItem(
					CONSTANTS.getString("type.attr")).getTextContent();
			col.setName(colName);
			col.setType(DatatypeFactory.getFactory()
					.getRegisteredDatatype(colType));
			for (int j = 0; j < colNode.getChildNodes().getLength(); j++) {
				Node row = colNode.getChildNodes().item(j);
				if (row instanceof Element == false) {
					continue;
				}
				Object entry = DatatypeFactory.getFactory().toObj(
						row.getTextContent(), colType);
				col.addEntry(entry);
			}
			table.addColumn(col);
		}
	}

	private File openTable(String dbName, String tableName)
			throws TableNotFoundException, DatabaseNotFoundException {
		File tableFile = new File(openDB(dbName), tableName
				+ CONSTANTS.getString("extension.xml"));
		if (!tableFile.exists()) {
			throw new TableNotFoundException();
		}
		return tableFile;
	}

	private File openDB(String dbName)
			throws DatabaseNotFoundException {
		File database = new File(WORKSPACE_DIR + File.separator + dbName);
		if (!database.exists()) {
			throw new DatabaseNotFoundException();
		}
		return database;
	}

	private void validateDB(Document doc, String dbName)
			throws TableNotFoundException {
		String db = doc.getElementsByTagName(CONSTANTS.getString(
				"table.element")).item(0).getAttributes().getNamedItem(
						CONSTANTS.getString("db.attr")).getTextContent();
		if (!db.equals(dbName)) {
			throw new TableNotFoundException();
		}
	}

	private void transform(Document doc, File tableFile, String tableName) {
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		DOMImplementation domImpl = doc.getImplementation();
		DocumentType doctype = domImpl.createDocumentType("doctype",
			"-//DBMS//DBMS v1.0//EN", tableName + CONSTANTS.getString("extensionDTD.schema"));
		transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
		DOMSource source = new DOMSource(doc);
		StreamResult result =
				new StreamResult(tableFile);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
