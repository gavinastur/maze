package com.fuori.games;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.fuori.games.Maze.Cell;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: gavinastur
 * Date: 20/02/15
 * Time: 11:20
 */
@RunWith(MockitoJUnitRunner.class)
public class MazeTest {

    @Mock
    private Maze.PathsAndHedges pathsAndHedges = mock(Maze.PathsAndHedges.class);

    @InjectMocks
    private Maze theMaze = new Maze();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testMazeContainsOnlyOneFinish() throws Exception {

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Maze must contain one Cell of type: F");

        String[][] mockGrid = new String[][]{
                {"X", "S", "X", "X"},
                {"X", " ", "X", " "},
                {"X", "F", "F", "X"},

        };

        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        theMaze.init();

    }

    @Test
    public void testMazeContainsAFinish() throws Exception {

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Maze must contain one Cell of type: F");

        String[][] mockGrid = new String[][]{
                {"X", "S", "X", "X"},
                {"X", " ", "X", " "},
                {"X", "X", "X", "X"},

        };

        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        theMaze.init();

    }

    @Test
    public void testMazeContainsAStart() throws Exception {

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Maze must contain one Cell of type: S");

        String[][] mockGrid = new String[][]{
                {"X", "S", "S", "X"},
                {"X", " ", " ", "X"},
                {"X", "F", "X", "X"},

        };
        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        theMaze.init();

    }

    @Test
    public void testMazeContainsAStart2() throws Exception {

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Maze must contain one Cell of type: S");

        String[][] mockGrid = new String[][]{
                {"X", " ", "X", "X"},
                {"X", " ", " ", "X"},
                {"X", "F", "X", "X"},

        };
        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        theMaze.init();

    }

    @Test
    public void testCountNumberOfHedges() throws Exception {

        final int expectedWallCount = 8;

        String[][] mockGrid = new String[][]{
                {"X", "S", "X", "X"},
                {"X", " ", " ", "X"},
                {"X", "F", "X", "X"},

        };

        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        int actualWallCount = theMaze.count(Cell.HEDGE);

        Assert.assertEquals("Maze wall count was not as expected", expectedWallCount, actualWallCount);

    }

    @Test
    public void testCountNumberOfPaths() throws Exception {

        final int expectedSpaceCount = 2;

        final String[][] mockGrid = new String[][]{
                {"X","S","X","X"},
                {"X"," "," ","X"},
                {"X","F","X","X"},

        };

        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        int actualWallCount = theMaze.count(Cell.PATH);

        Assert.assertEquals("Maze hedge count was not as expected", expectedSpaceCount, actualWallCount);

    }

    @Test
    public void testGetCellTypeForCoordinates() throws Exception {

        final Cell expectedCellType = Cell.PATH;

        // Given the following mock maze
        final String[][] mockGrid = new String[][]{
                {"X","S","X","X"},
                {"X"," "," ","X"},
                {"X","F","X","X"},

        };

        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        // When
        final Cell actualCellType = theMaze.getCellType(new Coordinate(1,1));

        // Then
        Assert.assertEquals("Maze Start coordinate was not as expected", expectedCellType, actualCellType);

    }

    @Test
    public void testGetStartCoordinates() throws Exception {

        // Given the following mock maze
        final String[][] mockGrid = new String[][]{
                {"X","X","S","X"},
                {"X"," "," ","X"},
                {"X","F","X","X"},

        };

        Coordinate expectedXY = new Coordinate(2,0);

        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        Coordinate actualXY = theMaze.getStartCoordinates();

        Assert.assertThat("Maze Start X coordinate was not as expected", expectedXY.getX(), is(actualXY.getX()));
        Assert.assertThat("Maze Start Y coordinate was not as expected", expectedXY.getY(), is(actualXY.getY()));


    }

    @Test
    public void testGetFinishCoordinates() throws Exception {

        // Given the following mock maze
        final String[][] mockGrid = new String[][]{
                {"X","S","X","X"},
                {"X"," "," ","X"},
                {"X","F","X","X"},

        };

        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        // When
        Coordinate actualFinish = theMaze.getFinishCoordinates();

        // Then
        Coordinate expectedXY = new Coordinate(1,2);
        Assert.assertThat("Maze Finish coordinates were not as expected", actualFinish.toString(), is(expectedXY.toString()));

    }

    @Test
    public void testNavigateForward() throws Exception {

        // Given the following mock maze
        final String[][] mockGrid = new String[][]{
                {"X","S","X","X"},
                {"X"," "," ","X"},
                {"X","F","X","X"},
        };

        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        // When I get the start position
        Coordinate start = theMaze.getStartCoordinates();

        // Then I should be able to move forward
        boolean movedForward = theMaze.moveForward(start.getY(), start.getX());

        Assert.assertThat("I should have moved forward but the result was ", movedForward,  is(true));
        Assert.assertThat("Coordinates were not as expected after moving", theMaze.getCurrentCoordinates().toString(),  is(new Coordinate(1,1).toString()));

    }

    @Test
    public void testNavigateBackward() throws Exception {

        // Given the following mock maze
        final String[][] mockGrid = new String[][]{
                {"X","S","X","X"},
                {"X","."," ","X"},
                {"X",".","*","X"},
                {"X","X"," ","X"},
                {"X","X","F","X"},
        };

        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);

        // When I get the start position
        Coordinate xy = theMaze.getCurrentCoordinates();

        // Then I should be able to move forward
        boolean movedBackward = theMaze.moveBackward(xy.getY(), xy.getX());

        Assert.assertThat("I should have moved backward but the result was ", movedBackward,  is(true));
        Assert.assertThat("Coordinates were not as expected after moving", theMaze.getCurrentCoordinates().toString(),  is(new Coordinate(2,1).toString()));

    }

    @Test
    public void testNavigateLeft() throws Exception {

        // Given the following mock maze
        final String[][] mockGrid = new String[][]{
                {"X","S","X","X"},
                {"X","*"," ","X"},
                {"X","F","X","X"},
        };

        // When I'm at a given position of 1,1
        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);
        Coordinate xy = theMaze.getCurrentCoordinates();

        // Then I should be able to move right
        boolean movedLeft = theMaze.moveLeft(xy.getY(), xy.getX());

        Assert.assertThat("I should have moved left but the result was ", movedLeft, is(true));
        Assert.assertThat("Coordinates were not as expected after moving", theMaze.getCurrentCoordinates().toString(),  is(new Coordinate(2,1).toString()));

    }

    @Test
    public void testNavigateRight() throws Exception {

        // Given the following mock maze
        final String[][] mockGrid = new String[][]{
                {"X","X","S","X"},
                {"X"," ","*","X"},
                {"X","F","X","X"},
        };

        // When I'm at a given position of 1,1
        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);
        Coordinate xy = theMaze.getCurrentCoordinates();

        // Then I should be able to move right
        boolean movedRight = theMaze.moveRight(xy.getY(), xy.getX());

        Assert.assertThat("I should have moved right but the result was ", movedRight, is(true));
        Assert.assertThat("Coordinates were not as expected after moving", theMaze.getCurrentCoordinates().toString(),  is(new Coordinate(1,1).toString()));

    }

    @Test
    public void testBreadcrumbs() throws Exception {

        // Given the following mock maze
        final String[][] mockGrid = new String[][]{
                {"X","S","X","X"},
                {"X"," ","X","X"},
                {"X"," "," ","X"},
                {"X","x"," ","X"},
                {"X"," "," ","X"},
                {"X","F","X","X"},
        };

        // When I'm at a given position from the start
        when(pathsAndHedges.getGrid()).thenReturn(mockGrid);
        Coordinate xy = theMaze.getStartCoordinates();

        // Then I should be able to Find A Way to the finish
        theMaze.moveForward(xy.getY(), xy.getX());
        xy = theMaze.getCurrentCoordinates();

        theMaze.moveForward(xy.getY(), xy.getX());
        xy = theMaze.getCurrentCoordinates();

        theMaze.moveLeft(xy.getY(), xy.getX());
        xy = theMaze.getCurrentCoordinates();

        theMaze.moveForward(xy.getY(), xy.getX());
        xy = theMaze.getCurrentCoordinates();

        theMaze.moveForward(xy.getY(), xy.getX());
        xy = theMaze.getCurrentCoordinates();

        theMaze.moveRight(xy.getY(), xy.getX());
        xy = theMaze.getCurrentCoordinates();

        theMaze.moveForward(xy.getY(), xy.getX());

        Assert.assertThat("I should have moved seven times", theMaze.getBreadcrumbs().size(), is(7));
        Assert.assertThat("Coordinates were not as expected for first breadcrumb", theMaze.getBreadcrumbs().get(0).toString(),  is(new Coordinate(1,0).toString()));
        Assert.assertThat("Coordinates were not as expected for second breadcrumb", theMaze.getBreadcrumbs().get(1).toString(),  is(new Coordinate(1,1).toString()));
        Assert.assertThat("Coordinates were not as expected for last breadcrumb", theMaze.getBreadcrumbs().get(6).toString(),  is(new Coordinate(1,4).toString()));

        theMaze.print();
    }


}
