package org.jspresso.hrsample.ext.frontend.export;

import org.jspresso.contrib.frontend.AbstractExportActiveResourceProvider;
import org.jspresso.contrib.frontend.ExportException;
import org.jspresso.contrib.frontend.IExportActiveResource;
import org.jspresso.contrib.model.bean.TableColumn;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.exception.BusinessException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ExportCsvActiveResourceProviderBean
 * User: Maxime HAMM
 * Date: 24/10/2017
 */
public class ExportCsvActiveResourceProviderBean extends AbstractExportActiveResourceProvider {

  private static final char QUOTE = '"';

  private Character separator = ',';

  @Override
  public IExportActiveResource createActiveResource(
          String fileName,
          List<TableColumn> selectedColumns,
          Iterator<?> iterator,
          Class<?> componentContract,
          String datePattern,
          String header,
          String footer,
          Integer maxLines,
          Map<String, Object> context) {

    IAccessorFactory accessorFactory = ((IBackendController) context.get(ActionContextConstants.BACK_CONTROLLER)).getAccessorFactory();

    return new ExportCsvActiveResource(
            fileName,
            selectedColumns,
            iterator,
            componentContract,
            datePattern,
            accessorFactory);

  }

  /**
   * Sets separator. Default is ','
   */
  public void setSeparator(Character separator) {
    this.separator = separator;
  }

  /*****************************************************
   * ExportCsvActiveResource
   */
  private class ExportCsvActiveResource implements IExportActiveResource {

    private final String fileName;
    private final List<TableColumn> selectedColumns;
    private final Iterator<?> iterator;
    private final Class<?> componentContract;
    private final String datePattern;
    private final IAccessorFactory accessorFactory;

    public ExportCsvActiveResource(
            String fileName,
            List<TableColumn> selectedColumns,
            Iterator<?> iterator,
            Class<?> componentContract,
            String datePattern,
            IAccessorFactory accessorFactory) {

      this.fileName = fileName;
      this.selectedColumns = selectedColumns;
      this.iterator = iterator;
      this.componentContract = componentContract;
      this.datePattern = datePattern;
      this.accessorFactory = accessorFactory;
    }

    @Override
    public void writeToContent(OutputStream output) throws IOException {

      SimpleDateFormat dateFormat = datePattern!= null ? new SimpleDateFormat(datePattern) : null;

      OutputStreamWriter writer = new OutputStreamWriter(output, Charset.forName("UTF-8"));
      try {

        while (iterator.hasNext()) {

          Object item = iterator.next();

          boolean first = true;
          for (TableColumn tc : selectedColumns) {

            if (!tc.isSelected())
              continue;

            if (!first)
              writer.write(separator);
            else
              first = false;

            // Gets back value from model
            IAccessor accessor = accessorFactory.createPropertyAccessor(tc.getId(), componentContract);
            Object v = accessor.getValue(item);
            if (v == null)
              continue;

            if (tc.getEnumName() != null) {
              String vv = tc.getEnumValues().get(v);
              if (vv == null || vv.trim().isEmpty())
                vv = v.toString();
              writer.write(vv);
            }
            else if (TableColumn.TYPE_DATE.equals(tc.getType()) && dateFormat!=null) {
              writer.write(dateFormat.format(v.toString()));
            }
            else {
              writer.write(escape(v.toString()));
            }
          }
          writer.write('\n');
        }
      } catch (BusinessException e) {
        throw e;
      } catch (Exception e) {
        throw new ExportException(this, "Export error", e);
      }

      writer.flush();
    }

    private String escape(String str) {

      StringBuilder sb = new StringBuilder();

      boolean needToSurroundWithQuote = false;
      for (char c : str.toCharArray()) {
        if (c == QUOTE) {
          sb.append(QUOTE);
        }
        sb.append(c);

        if (c == separator || c == QUOTE || c == '\n' || c == '\r')
          needToSurroundWithQuote = true;
      }

      if (needToSurroundWithQuote)
        sb.insert(0, QUOTE).append(QUOTE);

      return sb.toString();
    }

    @Override
    public String getHeader() {
      return null;
    }

    @Override
    public String getFooter() {
      return null;
    }

    @Override
    public String getMimeType() {
      return ExportCsvActiveResourceProviderBean.this.getMimeType();
    }

    @Override
    public String getName() {
      return fileName + "." + ExportCsvActiveResourceProviderBean.this.getFileExtension();
    }

    @Override
    public long getSize() throws IOException {
      return -1;
    }
  }
}
