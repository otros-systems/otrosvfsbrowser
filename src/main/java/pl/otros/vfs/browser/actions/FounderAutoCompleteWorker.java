package pl.otros.vfs.browser.actions;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.otros.vfs.browser.VfsBrowser;
import pl.otros.vfs.browser.util.VFSUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: Karol
 * Date: 10.12.13
 * Time: 17:10
 */
public class FounderAutoCompleteWorker extends SwingWorker<List<FileObject>, String> {
  private String currentFilePath;
  private DefaultComboBoxModel<String> pathModel;
  private FileObject currentFileObject;
  private String changedKey;
  private VfsBrowser currentBrowser;
  private static final Logger LOGGER = LoggerFactory.getLogger(VfsBrowser.class);

  public FounderAutoCompleteWorker(String filePath, DefaultComboBoxModel model, FileObject fileObject) {
    currentFilePath = filePath;
    pathModel = model;
    currentFileObject = fileObject;
  }

  @Override
  public List<FileObject> doInBackground() throws Exception {
    //TODO obsługa wylistowania plików
    String folder = currentFilePath.substring(0, currentFilePath.lastIndexOf('/'));
    String prefix = currentFilePath.substring(currentFilePath.lastIndexOf('/') + 1);
//        LOGGER.info("pathSelected: |" + currentFilePath + "| |" + prefix + "| currentPath: |"+currentFileObject.getName());
    currentFileObject = VFSUtils.resolveFileObject(folder);
    List<FileObject> properySelectedChildren = new ArrayList<FileObject>();
    FileObject[] children = currentFileObject.getChildren();
    for (FileObject child : children) {
      FileType type = child.getType();
      if (FileType.FOLDER.equals(type) || FileType.FILE_OR_FOLDER.equals(type)) {
        if (child.getName().getBaseName().startsWith(prefix)) {
          properySelectedChildren.add(child);
        }
      }
    }
    return properySelectedChildren;
  }

  @Override
  public void done() {
    pathModel.removeAllElements();
    try {
      for (FileObject file : get()) {
        pathModel.addElement(file.getName().toString());
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
//    pathModel.setSelectedItem(currentFilePath);
  }
}
