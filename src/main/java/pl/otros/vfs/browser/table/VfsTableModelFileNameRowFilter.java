package pl.otros.vfs.browser.table;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.regex.PatternSyntaxException;
import java.util.regex.Pattern;

public class VfsTableModelFileNameRowFilter extends RowFilter<VfsTableModel, Integer> {
  private static final Logger LOGGER =
        LoggerFactory.getLogger(VfsTableModelFileNameRowFilter.class);

  private JTextField textField;

  public VfsTableModelFileNameRowFilter(JTextField textField) {
    this.textField = textField;
  }

  @Override
  public boolean include(Entry<? extends VfsTableModel, ? extends Integer> entry) {
    String patternText = textField.getText();
    if (patternText.length()==0) return true;
    String baseName = entry.getModel().get(entry.getIdentifier()).getName().getBaseName();
    boolean result = checkIfInclude(baseName, patternText);
    return result;
  }

  boolean checkIfInclude(String baseName, String patternText) {
    Pattern pattern = null;
    try {
      String patternString = preparePatternString(patternText);
      pattern = Pattern.compile(patternString);
    } catch (PatternSyntaxException pse) {
      LOGGER.error(pse.getMessage());
      //if pattern can't be compiled we will use String contains test
    }
    LOGGER.debug(String.format("pattern=(%s)", pattern));
    return StringUtils.containsIgnoreCase(baseName, patternText) || (pattern == null) ? true : pattern.matcher(baseName).matches();
  }


  String preparePatternString(String patternText) {
    String patternString;
    if (patternText.charAt(0) == '/') {
      patternString = patternText.substring(1);
    } else {
      patternString=convertRegex(patternText);
    }
    return patternString;
  }

  String convertRegex(String patternText) {
    return "(?i)\\Q" + patternText
        .toString()
        .replaceAll("\\[[^]]+\\]", "\\\\E$0\\\\Q")
        .replaceAll("\\?", "\\\\E.\\\\Q")
        .replaceAll("\\*", "\\\\E.*\\\\Q");
  }
}
