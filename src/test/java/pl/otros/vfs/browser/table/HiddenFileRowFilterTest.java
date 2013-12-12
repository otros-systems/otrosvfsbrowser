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

/**
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 */
public class HiddenFileRowFilterTest {
    private VfsTableModel tableModel = new VfsTableModel();
    private JTable table = new JTable(tableModel);
    private RowFilter<VfsTableModel, Integer> noopFilter =
            new VfsTableModelHiddenFileRowFilter(true);
    private RowFilter<VfsTableModel, Integer> hideFilter =
            new VfsTableModelHiddenFileRowFilter(false);
    private TableRowSorter<VfsTableModel> sorter =
         new TableRowSorter<VfsTableModel>(tableModel);

    @DataProvider (name = "candidateFiles")
    public Object[][] arbitraryName01() {
        table.setRowSorter(sorter);
        return new Object[][]{
            {"/file01", true},
            {"/.file02", false},
            {"/file03.txt", true},
            {"/.file04.txt", false},
            {"/tmp/file05.txt", true},
            {"/tmp/.file06.txt", false},
            {"\\tmp\\file07.txt", true},
            {"\\tmp\\.file08.txt", false},
            {"file:///tmp/file09.txt", true},
            {"file:///tmp/.file10.txt", false},
       };
    }

    @Test (dataProvider = "candidateFiles")
    public void testNoHiding(String filepath, boolean unused)
    throws FileSystemException {
        tableModel.setContent(new FileObject[] {
            VFSUtils.resolveFileObject(filepath)
        });
        sorter.setRowFilter(noopFilter);
        assertEquals(tableModel.getRowCount(), 1,
                "Problem with datamodel population");

        /* Some sample code in case need to test retained filename.
        Object cellVal = tableModel.getValueAt(ind, VfsTableModel.COLUMN_NAME);
        if (!(cellVal instanceof FileNameWithType))
            throw new RuntimeException(String.format(
                        "Name column not 'FlieNameWithType:  %s",
                        cellVal.getClass().getName()));
        assertEquals(((FileNameWithType) cellVal).getFileName(), "x",
                String.format("Name (%s), count %d", 
        ((FileNameWithType) cellVal).getFileName(), tableModel.getRowCount()));
        */
        assertEquals(table.convertRowIndexToView(0), 0, String.format(
                "File name '%s' filtered out with no-hide setting", filepath));
    }

    @Test (dataProvider = "candidateFiles")
    public void testHiding(String filepath, boolean shouldReveal)
    throws FileSystemException {
        tableModel.setContent(new FileObject[] {
            VFSUtils.resolveFileObject(filepath)
        });
        sorter.setRowFilter(hideFilter);
        assertEquals(tableModel.getRowCount(), 1,
                "Problem with datamodel population");

        assertEquals(table.convertRowIndexToView(0),
                shouldReveal ? 0 : -1, String.format(
                "File name '%s' filtered out with no-hide setting", filepath));
    }
}
