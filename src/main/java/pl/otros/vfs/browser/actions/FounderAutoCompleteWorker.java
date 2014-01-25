package pl.otros.vfs.browser.actions;

import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.otros.vfs.browser.VfsBrowser;

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
public class FounderAutoCompleteWorker extends SwingWorker<List<FileObject>, String>{

    private String currentFilePath;
    private DefaultComboBoxModel<String> pathModel;
    private FileObject currentFileObject;
    private String changedKey;
    private VfsBrowser currentBrowser;

    private static final Logger LOGGER = LoggerFactory.getLogger(VfsBrowser.class);

    public FounderAutoCompleteWorker(String filePath, DefaultComboBoxModel model, FileObject fileObject){
        currentFilePath = filePath;
        pathModel = model;
        currentFileObject = fileObject;
    }



    @Override
    protected List<FileObject> doInBackground() throws Exception {
        //TODO obsługa wylistowania plików
        String prefix = currentFilePath.substring(currentFileObject.getName().toString().length()+1);
        LOGGER.info("pathSelected: |" + currentFilePath + "| |" + prefix + "| currentPath: |"+currentFileObject.getName());
        List<FileObject> properySelectedChildren = new ArrayList<FileObject>();
        if(currentFileObject.getType().equals(FileType.FILE_OR_FOLDER) || currentFileObject.getType().equals(FileType.FOLDER)){
            for(FileObject fileObject : currentFileObject.getChildren()){
                //LOGGER.info("checked: "+fileObject.getName().getBaseName());
                if(fileObject.getName().getBaseName().startsWith(prefix))
                    properySelectedChildren.add(fileObject);
            }
        }
        return properySelectedChildren;
    }

    @Override
    public void done(){
        pathModel.removeAllElements();
        try {
            for(FileObject file : get())
                pathModel.addElement(file.getName().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        pathModel.setSelectedItem(currentFilePath);
    }


}
