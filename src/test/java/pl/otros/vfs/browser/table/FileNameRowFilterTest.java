package pl.otros.vfs.browser.table;

import static org.testng.Assert.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import javax.swing.RowFilter;
import pl.otros.vfs.browser.util.VFSUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 */
public class FileNameRowFilterTest {
    private VfsTableModel tableModel = new VfsTableModel();
    private JTable table = new JTable(tableModel);
    private JTextField textField = new JTextField();
    private RowFilter<VfsTableModel, Integer> filter =
            new VfsTableModelFileNameRowFilter(textField);
    private TableRowSorter<VfsTableModel> sorter =
         new TableRowSorter<VfsTableModel>(tableModel);

    @DataProvider (name = "patternTests")
    public Object[][] arbitraryName01() {
        table.setRowSorter(sorter);
        return new Object[][]{
            {"/file01", "file", false},
            {"/file01", "FILE", false},
            {"/FILE01", "file", false},
            {"/file02", "file*", true},
            {"/file03.txt", "file[456]*", false},
            {"/file04.txt", "file[4-678]*", false},
            {"/tmp/file05.txt", "file[0-3]*", true},
            {"/tmp/FILE05.txt", "file[0-3]*", true},
            {"/tmp/file05.txt", "FILE[0-3]*", true},
            {"/tmp/file05.txt", "FiLe[0-3]*", true},
            {"/tmp/file06.txt", "file[09][5-8]*", true},
            {"\\tmp\\file07.TXT", "File*Txt", true},
            {"\\tmp\\file08.txt", "file*wrong", false},
            {"file:///tmp/file09.txt", "file?9.txt", true},
            {"file:///tmp/file10.txt", "file??9.txt", false},
            {"file:///tmp/file10.txt", "file??*", true},
            {"file:///tmp/file11", "file??*", true},
            {"file:///tmp/file111", "file???*", true},
            {"file:///tmp/file1112", "file???*", true},
            //VFS resolveFile() chokes on following:
            //{"file:///tmp/@%!=.txt", "*.txt", true},
            {"file:///tmp/(file1))3.txt=", "*.txt?", true},
            {"file:///tmp/file13.txt", "/\\.txt", false},
            {"file:///tmp/file14.txt", "/.+\\.txt", true},
            {"file:///tmp/file15.txt", "/.*\\.txt", true},
            {"file:///tmp/(file1))3.txt=", "/.*\\.txt.", true},
            {"file:///tmp/(File1))3.txt=", "/.*\\.TXT.", false},
            {"file:///tmp/(File1))3.txt=", "/(?i).*\\.TXT.", true},
            {"file:///tmp/(File1))3.txt=", "/(?i).*\\.TXT[@=b]", true},
            //VFS resolveFile() chokes on following:
            //{"file:///tmp/@%!.txt=", "/.*\\.txt.", true},
       };
    }

    @Test (dataProvider = "patternTests")
    public void testFileNamePattern(
            String filepath, String pattern, boolean shouldMatch)
    throws FileSystemException {
        FileObject fileObject = VFSUtils.resolveFileObject(filepath);
        tableModel.setContent(new FileObject[] { fileObject });
        textField.setText(pattern);
        sorter.setRowFilter(filter);

        assertEquals(tableModel.getRowCount(), 1,
                "Problem with datamodel population");
        assertEquals(table.convertRowIndexToView(0),
                shouldMatch ? 0 : -1, String.format(
                "File name '%s' unexpectedly %s pattern '%s'.  Basename (%s).",
                filepath,
                    ((table.convertRowIndexToView(0) == 0)
                    ? "matched" 
                    : "did not match"),
                pattern, fileObject.getName().getBaseName()));
    }
}
