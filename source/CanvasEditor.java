/**
 * Side Notes:
 * - Contains no personal student information.
 *   (However, does depict certain aspects of the
 *   algorithm used for naming conventions)
 * - Uses java.nio features implemented in java version 1.7
 * - No javadaoc support
 * 
 * To Do:
 * - Add a date time stamp to output folder name.
 *   (allows for multiple folder creations)
 * - Use http://commons.apache.org/proper/commons-cli/
 *   for command line arguments.
 * - Add support for more file types
 *   (both to extraction, ex allow compressed files
 *   and also to command line specification)
 * - Add more code documentation / examples
 * - Add java doc 
 */
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CanvasEditor {
	
	private static Path TARGET_DIRECTORY
		= Paths.get(System.getProperty("user.dir"));
	private static int DEPTH_LEVEL = -1;
	private static String OUTPUT_FOLDER_NAME = "AllStudents";
	
	private static String OUT_PARENT;
	private static String FILE_EXTENSION = "*.{java}"; // "*.{txt, java, cpp, h}"
	
	private static List<Path> TARGET_FILES;
	private static List<String> STUDENT_DIRS;
	private static List<Path> RESULT_DIRS;
	
	private static final String PATH_SEP = System.getProperty("file.separator");
	private static final String LINE_SEP = System.getProperty("line.separator");
	
	public static void main(String[] args) throws Exception {
		final int argsLen = args.length;
		parseArgs(argsLen, args);
		TARGET_FILES = new LinkedList<Path>(listFiles());
		makeOutDir();
		makeOutDirStudents();
		makeSubFolders();
	}
	
	private static void makeOutDir() throws IOException {
		if (TARGET_DIRECTORY == null) {
			System.err.println(LINE_SEP + "Target directory not found");
		}
		Path parent = null;
		if (Files.isDirectory(TARGET_DIRECTORY)) {
			parent = TARGET_DIRECTORY;
		} else {
			parent = TARGET_DIRECTORY.getParent();
		}
		OUT_PARENT = parent.toString() + PATH_SEP + OUTPUT_FOLDER_NAME;
		if (OUT_PARENT == null) {
			System.err.println(LINE_SEP + "No OUT_PARENT folder");
		}
		Files.createDirectory(Paths.get(OUT_PARENT));
	}
	
	private static void makeOutDirStudents() throws IOException {
		if (TARGET_FILES == null || TARGET_FILES.size() == 0) {
			System.err.println(LINE_SEP + "Target files list empty");
		}
		STUDENT_DIRS = new LinkedList<String>();
		for (Path p : TARGET_FILES) {
			String student = parseStudentsName(p.toString());
			int i = lastIndexOfPathSep(student);
			student = student.substring(i, student.length());
			if (!STUDENT_DIRS.contains(student)) {
				STUDENT_DIRS.add(student);
			}
		}
		RESULT_DIRS = new LinkedList<Path>();
		for (String s : STUDENT_DIRS) {
			final Path p = Paths.get(OUT_PARENT + PATH_SEP + s);
			RESULT_DIRS.add(p);
			Files.createDirectory(p);
		}
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method listFiles
	 * @author Nick Bell
	 * @description Custom method for obtaining necessary files.
	 * @return List of paths
	 * @throws IOException
	 */
	public static List<Path> listFiles() throws IOException {
		List<Path> result = new LinkedList<Path>();
		Stack<Path> stack = new Stack<Path>();
		stack.push(TARGET_DIRECTORY);
		int depthLevel = 0;
		DirectoryStream<Path> stream = null;
		while (!stack.isEmpty()) {
			stream = Files.newDirectoryStream(stack.pop(), FILE_EXTENSION);
			depthLevel++;
			for (Path p : stream) {
				if (Files.isDirectory(p))
					stack.push(p);
				if (p.toFile().isFile())
					result.add(p);
			}
			if (depthLevel == DEPTH_LEVEL)
				break;
		}
		stream.close();
		return result;
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method makeSubFolders
	 * @author Nick Bell
	 * @description Intended to create actual .java files in respective
	 * students own project directory.
	 * @throws IOException
	 */
	private static void makeSubFolders() throws IOException {
		if (TARGET_FILES == null) {
			throw new NullPointerException("makeSubFiles method");
		}
		for (Path p : TARGET_FILES) {
			String ss = parseStudentsName(p.toString());
			ss = ss.substring(lastIndexOfPathSep(ss) + 1, ss.length());
			for (Path z : RESULT_DIRS) {
				if (!z.toString().contains(ss)) {
					continue;
				}
				String k = parseJavaSRCFileName(p.toString()) + ".java";
				Path pq = Paths.get(z.toString() + PATH_SEP + k);
				Files.createDirectory(pq);
				final CopyOption co = StandardCopyOption.REPLACE_EXISTING;
				Files.copy(p, pq, co);
				break;
			}
		}
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method parsePass1
	 * @author Nick Bell
	 * @description Obtains the students name. May also return a suffix
	 * to the result, "--late", if the given submission is turned in late.
	 * @param s Target string text
	 * @return Parsed string text
	 */
	private static String parseStudentsName(final String s) {
		if (s == null) {
			return null;
		}
		final int idx = s.indexOf('_');
		final String student = s.substring(0, idx);
		return student;
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method parseJavaSRCFileName
	 * @author Nick Bell
	 * @description Obtains the java source file name, respective to the
	 * class name.
	 * @param s Target string text
	 * @return Parsed string text
	 * @throws ArrayIndexOutOfBoundsException Thrown if given invalid args
	 */
	private static String parseJavaSRCFileName(final String s)
			throws ArrayIndexOutOfBoundsException {
		if (s == null) {
			return null;
		}
		final int trashIdx1 = s.indexOf('_');
		final int trashIdx2 = s.indexOf('_', trashIdx1 + 1);
		final int trashIdx3 = s.indexOf('_', trashIdx2 + 1);
		final int trailingDelimeter = s.lastIndexOf('.');
		String result = s.substring(trashIdx3 + 1, trailingDelimeter);
		result = removeCopyIdentifierChars(result);
		return result;
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method removeNonNamingChars
	 * @author Nick Bell
	 * @description Removes invalid characters based on compliance standards
	 * @param in Target string of text
	 * @return Potential edited version of string text
	 */
	private static String removeCopyIdentifierChars(final String s)
			throws ArrayIndexOutOfBoundsException {
		final int idx = s.indexOf('-');
		if (idx == -1) {
			return s;
		}
		return s.substring(0, idx);
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method getPathName
	 * @author Nick Bell
	 * @description Gets the .../<"FILE NAME">*
	 * @param p Target path to use
	 * @return Stripped "FILE NAME"
	 */
	private static String getPathName(final Path p) {
		if (p == null) {
			return null;
		}
		final String s = p.toAbsolutePath().toString();
		return getFileName(s);
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method getFileName
	 * @author Nick Bell
	 * @description Gets the .../<"FILE NAME">*
	 * @param s Target string absolute path to use
	 * @return Stripped "FILE NAME"
	 */
	private static String getFileName(final String s) {
		if (s == null) {
			return null;
		}
		final int i = lastIndexOfPathSep(s);
		final String result = s.substring(0, i + 1);
		return result;
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method lastIndexOfPathSep
	 * @author Nick Bell
	 * @description Gets the index of the last occurrence of the platforms
	 * file separator
	 * @param str Target string
	 * @return Index occurrence
	 */
	private static int lastIndexOfPathSep(final String str) {
		if (str == null) {
			return -1; // Not found
		}
		final int idx = str.lastIndexOf(PATH_SEP);
		return idx;
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method printUsage
	 * @author Nick Bell
	 * @description Prints command line usage
	 */
	private static final void printUsage() {
		String s = "Basic Usage: \"x <Target Submission Folder>" +
				"<Directory Search Depth> <Output Folder Name (not path)>";
		System.out.println(s);
		System.exit(-1);
	}
	
	// -----------------------------------------------------------------------
	/**
	 * @method printException
	 * @author Nick Bell
	 * @description Helpful debug for displaying exceptions
	 * @param exception Exception to handled
	 */
	private static final void printException(Exception exception) {
		final String err = exception.getMessage();
		System.err.printf("Error: %s" + LINE_SEP, err);
		exception.printStackTrace();
		System.err.println(LINE_SEP + "--> Stack trace done <--");
	}
	
	private static final void parseArgs(final int l, final String[] arr) {
		
		// Default case
		if (l == 0 || arr == null || l != arr.length) {
			return;
		}
		
		switch (l) {
		case 3:
			OUTPUT_FOLDER_NAME = arr[2];
			if (OUTPUT_FOLDER_NAME == null) {
				printUsage();
			}
			Path test = Paths.get(OUTPUT_FOLDER_NAME);
			if (test == null) {
				printUsage();
			}
		case 2:
			try {
				DEPTH_LEVEL = Integer.valueOf(arr[1]);
			} catch (NumberFormatException e) {
				printException(e);
				printUsage();
			}
		case 1:
			// Handle target submission directory
			TARGET_DIRECTORY = Paths.get(arr[0].replaceAll("\"", ""));
			if (TARGET_DIRECTORY == null) {
				printUsage();
			}
			break;
		default:
			printUsage();
			break;
		}
	}
}
