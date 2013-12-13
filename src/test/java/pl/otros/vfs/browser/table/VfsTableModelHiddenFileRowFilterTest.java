package pl.otros.vfs.browser.table;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VfsTableModelHiddenFileRowFilterTest {


  @DataProvider(name = "candidateFiles")
  public Object[][] arbitraryName01() {
    return new Object[][]{
        {"_file01", true, true, true},
        {".file01", true, true, true},
        {"_file01", false, true, true},
        {".file01", false, true, true},

        {"_file01", true, false, false},
        {".file01", true, false, false},
        {"_file01", false, false, true},
        {".file01", false, false, false},


    };
  }

  @Test(dataProvider = "candidateFiles")
  public void testCheckIfInclude(String path, boolean hiddenAttribute, boolean showHidden, boolean expected) throws Exception {
    //given
    VfsTableModelHiddenFileRowFilter rowFilter = new VfsTableModelHiddenFileRowFilter(showHidden);
    FileObject fileObject = mock(FileObject.class);
    FileName fileName = mock(FileName.class);
    when(fileObject.getName()).thenReturn(fileName);
    when(fileName.getBaseName()).thenReturn(path);
    when(fileObject.isHidden()).thenReturn(hiddenAttribute);

    //when
    boolean result = rowFilter.checkIfInclude(fileObject);

    //then
    Assert.assertEquals(result, expected, String.format("Result for file \"%s\" with hidden attribute %s and showHidden checked %s should " +
        "be %s, was %s", path,
        hiddenAttribute, showHidden, result, expected));

  }
}
