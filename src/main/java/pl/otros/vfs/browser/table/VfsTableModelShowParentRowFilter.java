package pl.otros.vfs.browser.table;

import org.apache.commons.vfs2.FileObject;
import pl.otros.vfs.browser.ParentFileObject;

import javax.swing.*;

public class VfsTableModelShowParentRowFilter extends RowFilter<VfsTableModel, Integer> {

  @Override
  public boolean include(Entry<? extends VfsTableModel, ? extends Integer> entry) {
    Integer identifier = entry.getIdentifier();
    FileObject fileObject = entry.getModel().get(identifier);
    return ParentFileObject.PARENT_NAME.equals(fileObject.getName().getBaseName());
  }
}
