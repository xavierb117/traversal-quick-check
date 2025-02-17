import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuickCheckTest {

  // We will capture System.out in these tests to verify output of print methods
  // You do not need to memorize how to do this, but look at it with curiosity!
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  // Before each test set up a fake system out to capture output
  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(new TeeOutputStream(outContent, originalOut)));
  }

  // After each test go back to the real system out
  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
    outContent.reset();
  }



  @Test
  void testPrintLongerThan7NullNode() {
    QuickCheck.printLongerThan7(null);
    assertEquals("", outContent.toString(), "Expected no output for null node");
  }

  @Test
  void testPrintLongerThan7SingleShortString() {
    TreeNode<String> root = new TreeNode<>("Short");
    QuickCheck.printLongerThan7(root);
    assertEquals("", outContent.toString(), "Expected no output since 'Short' length is <= 7");
  }

  @Test
  void testPrintLongerThan7SingleLongString() {
    TreeNode<String> root = new TreeNode<>("LongStringExample");
    QuickCheck.printLongerThan7(root);
    String output = outContent.toString().trim();
    assertEquals("LongStringExample", output, "Should print the single long string");
  }

  @Test
  void testPrintLongerThan7LinkedListStyle() {
    TreeNode<String> root = new TreeNode<>("HelloWorld");
    root.right = new TreeNode<>("Chain");
    root.right.right = new TreeNode<>("LongEnoughWord");
    root.right.right.right = new TreeNode<>("Tiny");

    QuickCheck.printLongerThan7(root);

    String output = outContent.toString().trim();
    String[] lines = output.split("\\r?\\n");
    assertEquals(2, lines.length, "Two strings should have been printed");
    assertEquals("HelloWorld", lines[0]);
    assertEquals("LongEnoughWord", lines[1]);
  }

  @Test
  void testPrintLongerThan7MultiLevelTree() {
    TreeNode<String> root = new TreeNode<>("RootNodeIsLong!");
    root.left = new TreeNode<>("Short");
    root.right = new TreeNode<>("IntermediateLen");
    root.left.left = new TreeNode<>("XYZword");
    root.left.right = new TreeNode<>("AnotherLongString!");
    root.right.right = new TreeNode<>("Mini");

    QuickCheck.printLongerThan7(root);

    String output = outContent.toString().trim();
    String[] lines = output.split("\\r?\\n");
    assertEquals(3, lines.length, "Three strings should have been printed");
    assertEquals("RootNodeIsLong!", lines[0], "First printed should be root in pre-order");
    assertEquals("AnotherLongString!", lines[1], "Should appear after visiting left subtree's right child");
    assertEquals("IntermediateLen", lines[2], "Then visiting the right child in pre-order");
  }

  @Test
  void testOddSumNullNode() {
    int result = QuickCheck.oddSum(null);
    assertEquals(0, result, "For a null node, the sum of odd values should be 0");
  }

  @Test
  void testOddSumSingleOddValue() {
    TreeNode<Integer> root = new TreeNode<>(15);
    int result = QuickCheck.oddSum(root);
    assertEquals(15, result, "Sum of a single odd node '15' should be 15");
  }

  @Test
  void testOddSumSingleEvenValue() {
    TreeNode<Integer> root = new TreeNode<>(100);
    int result = QuickCheck.oddSum(root);
    assertEquals(0, result, "Sum should be 0 if the single node has an even value");
  }

  @Test
  void testOddSumLinkedListStyle() {
    TreeNode<Integer> root = new TreeNode<>(11);
    root.right = new TreeNode<>(42);
    root.right.right = new TreeNode<>(19);
    root.right.right.right = new TreeNode<>(88);
    root.right.right.right.right = new TreeNode<>(101);

    int result = QuickCheck.oddSum(root);
    assertEquals(131, result, "Sum of 11 + 19 + 101 = 131");
  }

  @Test
  void testOddSumMultiLevelTree() {
    TreeNode<Integer> root = new TreeNode<>(7);
    root.left = new TreeNode<>(-3);
    root.right = new TreeNode<>(14);
    root.left.left = new TreeNode<>(19);
    root.left.right = new TreeNode<>(2);
    root.right.left = new TreeNode<>(25);
    root.right.right = new TreeNode<>(100);

    int result = QuickCheck.oddSum(root);
    assertEquals(48, result, "Sum of odd values in multi-level tree should be 48");
  }


  // Used for testing purposes so you can still see your print statements when debugging
  // You do not need to modify this 
  static class TeeOutputStream extends OutputStream {
    private final OutputStream first;
    private final OutputStream second;

    public TeeOutputStream(OutputStream first, OutputStream second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public void write(int b) {
      try {
        first.write(b);
        second.write(b);
      } catch (Exception e) {
        throw new RuntimeException("Error writing to TeeOutputStream", e);
      }
    }

    @Override
    public void flush() {
      try {
        first.flush();
        second.flush();
      } catch (Exception e) {
        throw new RuntimeException("Error flushing TeeOutputStream", e);
      }
    }

    @Override
    public void close() {
      try {
        first.close();
        second.close();
      } catch (Exception e) {
        throw new RuntimeException("Error closing TeeOutputStream", e);
      }
    }
  }
}
