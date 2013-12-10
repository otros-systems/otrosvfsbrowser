package pl.otros.vfs.browser.table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs2.FileObject;
import pl.otros.vfs.browser.VfsBrowser;

import javax.swing.*;
import java.util.regex.PatternSyntaxException;

public class VfsTableModelFileNameRowFilter extends RowFilter<VfsTableModel, Integer> {

  private JTextField textField;

  public VfsTableModelFileNameRowFilter(JTextField textField) {

    this.textField = textField;
  }

  @Override
  public boolean include(Entry<? extends VfsTableModel, ? extends Integer> entry) {
    String text = textField.getText();
    if (text.length()==0){
      return true;
    }
    Integer identifier = entry.getIdentifier();
    FileObject fileObject = entry.getModel().get(identifier);
    String baseName = fileObject.getName().getBaseName();
    boolean result = true;
    try {
      //TODO ignore case
      result = baseName.matches(text) || StringUtils.containsIgnoreCase(baseName, text);
      //TODO set filterField normal background
    } catch (PatternSyntaxException e) {
      //TODO set filterField error background
    }
    return result;
  }
}
