package pl.otros.vfs.browser.table;

import org.apache.commons.lang.StringUtils;
import pl.otros.vfs.browser.VfsBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
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
    Pattern pattern = null;
    try {
      if (patternText.charAt(0) == '/') {
        pattern = Pattern.compile(patternText.substring(1));
      } else {
        pattern = Pattern.compile("(?i)\\Q" + patternText
            .toString()
            .replaceAll("\\[[^]]+\\]", "\\\\E$0\\\\Q")
            .replaceAll("\\?", "\\\\E.\\\\Q")
            .replaceAll("\\*", "\\\\E.*\\\\Q"));
      }

    } catch (PatternSyntaxException pse) {
      LOGGER.error(pse.getMessage());
      //TODO set filterField error background and give it focus
      //If we are using contains as alternative to regex, maybe whe should not mark text field red.
    }
    LOGGER.debug(String.format("pattern=(%s)", pattern));
    return StringUtils.containsIgnoreCase(baseName,patternText) || (pattern == null) ? true : pattern.matcher(baseName).matches();
  }
}
