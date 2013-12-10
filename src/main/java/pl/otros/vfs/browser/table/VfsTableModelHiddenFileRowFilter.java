package pl.otros.vfs.browser.table;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import javax.swing.*;

public class VfsTableModelHiddenFileRowFilter extends RowFilter<VfsTableModel, Integer> {
  private boolean showHidden;

  public VfsTableModelHiddenFileRowFilter(boolean showHidden) {
    this.showHidden = showHidden;
  }

  @Override
  public boolean include(Entry<? extends VfsTableModel, ? extends Integer> entry) {
    Integer identifier = entry.getIdentifier();
    FileObject fileObject = entry.getModel().get(identifier);
    if (!showHidden) {
      try {
        if (fileObject.getName().getBaseName().startsWith(".") || fileObject.isHidden()) {
          return false;
        }
      } catch (FileSystemException e) {

      }
    }
    return true;
  }
}
