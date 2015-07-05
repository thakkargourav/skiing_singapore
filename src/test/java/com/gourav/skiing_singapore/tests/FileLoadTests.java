package com.gourav.skiing_singapore.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.gourav.skiing_singapore.model.ElevationMap;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * @author Gourav Thakkar (gourav@evivehealth.com)
 */
public class FileLoadTests {


    @Test
    public void testValidFile() {
        final ElevationMap elevationMap = ElevationMap.fromFile(Paths.get("src/test/resources", "validtestmap.txt"));
        assertThat(elevationMap).isNotNull();
        assertThat(elevationMap.getColumnSize()).isEqualTo(4);
        assertThat(elevationMap.getRowSize()).isEqualTo(4);
        assertThat(elevationMap.getElevations()).isNotEmpty().hasSize(4);

        final ElevationMap elevationMapMxN = ElevationMap.fromFile(Paths.get("src/test/resources", "validtestmapMxN.txt"));
        assertThat(elevationMapMxN).isNotNull();
        assertThat(elevationMapMxN.getColumnSize()).isEqualTo(5);
        assertThat(elevationMapMxN.getRowSize()).isEqualTo(4);
        assertThat(elevationMapMxN.getElevations()).isNotEmpty().hasSize(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFile() {
        final ElevationMap elevationMap = ElevationMap.fromFile(Paths.get("src/test/resources", "emptytestmap.txt"));
        assertThat(elevationMap).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithWrongColumns() {
        final ElevationMap elevationMap =
                ElevationMap.fromFile(Paths.get("src/test/resources", "wrongcolumntestmap.txt"));
        assertThat(elevationMap).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithWrongRows() {
        final ElevationMap elevationMap = ElevationMap.fromFile(Paths.get("src/test/resources", "wrongrowtestmap.txt"));
        assertThat(elevationMap).isNull();
    }

    @Test(expected = NumberFormatException.class)
    public void testFileWithWrongData() {
        final ElevationMap elevationMap =
                ElevationMap.fromFile(Paths.get("src/test/resources", "wrongdatatestmap.txt"));
        assertThat(elevationMap).isNull();
    }

    @Test
    public void testMissingFile() {
        final ElevationMap elevationMap = ElevationMap.fromFile(Paths.get("src/test/resources", "missing.txt"));
        assertThat(elevationMap).isNull();
    }

}
