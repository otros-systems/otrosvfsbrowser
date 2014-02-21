package pl.otros.vfs.browser.list;

import org.apache.commons.vfs2.FileObject;
import pl.otros.vfs.browser.actions.FounderAutoCompleteWorker;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: Karol
 * Date: 18.02.14
 * Time: 19:38
 */
public class WriteLetterDocumetListener implements DocumentListener {

    private JTextField textField;
    private int DELAY_TIME = 1000;
    private long lastTextFieldEditTime;
    private JComboBox pathField;
    private DefaultComboBoxModel pathModel;
    private FileObject currentLocation;
    private Future pathAutoCompleteTask;
    private ExecutorService pathAutoCompleteExecutor;

    public WriteLetterDocumetListener(JTextField textField, JComboBox pathField, DefaultComboBoxModel pathModel, FileObject currentLocation, Future pathAutoCompleteTask, ExecutorService pathAutoCompleteExecutor){
        this.textField = textField;
        this.pathField = pathField;
        this.pathModel = pathModel;
        this.currentLocation = currentLocation;
        this.pathAutoCompleteTask = pathAutoCompleteTask;
        this.pathAutoCompleteExecutor = pathAutoCompleteExecutor;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        System.out.println("INSERT");
        invokeDocumentChange(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        System.out.println("REMOVE");
        invokeDocumentChange(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    private void invokeDocumentChange(final DocumentEvent event){
        lastTextFieldEditTime = System.currentTimeMillis();
        Timer timer = new Timer(DELAY_TIME, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(System.currentTimeMillis()-lastTextFieldEditTime >= DELAY_TIME){
                    if(DocumentEvent.EventType.INSERT.equals(event.getType())){
                        System.out.println("'"+textField.getText().trim()+"'");
                        if (pathAutoCompleteTask != null)
                            pathAutoCompleteTask.cancel(true);
                        String extendedPath = pathField.getSelectedItem().toString();
                        System.out.println(extendedPath);
                        //Runnable autoCompleteWorker = new FounderAutoCompleteWorker(extendedPath, pathModel, currentLocation);
                        //pathAutoCompleteTask = pathAutoCompleteExecutor.submit(autoCompleteWorker);
                    }
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
