package com.chat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class for file operations and management.
 * 
 * This class provides comprehensive file handling utilities including:
 * - File name generation
 * - File validation
 * - File type checking
 * - Safe file operations
 * 
 * All methods include proper exception handling and logging.
 * 
 * @author Book Social App Team
 * @version 1.0
 * @since 1.0
 */
public class FileUtils {

    // Logger instance for all file operations
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    // Constants for file validation
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final Pattern VALID_FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");

    /**
     * Private constructor to prevent instantiation of utility class.
     * This class contains only static methods and should not be instantiated.
     */
    private FileUtils() {
        // Prevent instantiation
        throw new AssertionError("Cannot instantiate utility class");
    }

    // ============================= FILE NAME GENERATION =============================

    /**
     * Generates a unique file name with UUID and specified extension.
     * 
     * Creates a unique file name using UUID to avoid collisions.
     * Format: {uuid}.{extension}
     * 
     * @param extension the file extension without dot (e.g., "jpg", "pdf")
     * @return a unique file name with extension
     * @throws IllegalArgumentException if extension is invalid
     */
    public static String generateUniqueFileNameWithExtension(String extension) {
        // Example: generateUniqueFileNameWithExtension("jpg") => "550e8400-e29b-41d4-a716-446655440000.jpg"
        try {
            if (extension == null || extension.trim().isEmpty()) {
                LOGGER.warn("Extension is null or empty, generating name without extension");
                return UUID.randomUUID().toString();
            }

            String cleanExtension = extension.trim().toLowerCase();
            if (cleanExtension.startsWith(".")) {
                cleanExtension = cleanExtension.substring(1);
            }

            if (!VALID_FILENAME_PATTERN.matcher(cleanExtension).matches()) {
                throw new IllegalArgumentException(
                    "Invalid extension format: " + extension + ". Only alphanumeric, dots, hyphens, and underscores allowed."
                );
            }

            String uniqueFileName = UUID.randomUUID() + "." + cleanExtension;
            LOGGER.debug("Generated unique file name: {}", uniqueFileName);
            return uniqueFileName;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Error generating unique file name with extension: {}", extension, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error generating unique file name", e);
            throw new RuntimeException("Failed to generate unique file name", e);
        }
    }

    /**
     * Generates a unique file name without extension using UUID.
     * 
     * @return a unique file name without extension
     */
    public static String generateUniqueFileName() {
        // Example: generateUniqueFileName() => "550e8400-e29b-41d4-a716-446655440000"
        try {
            String uniqueFileName = UUID.randomUUID().toString();
            LOGGER.debug("Generated unique file name without extension: {}", uniqueFileName);
            return uniqueFileName;
        } catch (Exception e) {
            LOGGER.error("Unexpected error generating unique file name", e);
            throw new RuntimeException("Failed to generate unique file name", e);
        }
    }

    // ============================= FILE VALIDATION =============================

    /**
     * Validates if a file exists and is readable.
     * 
     * Checks if the file path exists and has read permissions.
     * 
     * @param filePath the path to the file to validate
     * @return true if file exists and is readable, false otherwise
     */
    public static boolean isFileValid(String filePath) {
        // Example: isFileValid("/path/to/file.txt") => true/false
        try {
            if (filePath == null || filePath.trim().isEmpty()) {
                LOGGER.warn("File path is null or empty");
                return false;
            }

            File file = new File(filePath);
            boolean isValid = file.exists() && file.isFile() && file.canRead();

            if (!isValid) {
                LOGGER.warn("File validation failed for path: {}. Exists: {}, IsFile: {}, CanRead: {}",
                    filePath, file.exists(), file.isFile(), file.canRead());
            } else {
                LOGGER.debug("File validation passed for path: {}", filePath);
            }

            return isValid;

        } catch (SecurityException e) {
            LOGGER.error("Security exception during file validation for path: {}", filePath, e);
            return false;
        } catch (Exception e) {
            LOGGER.error("Unexpected error validating file: {}", filePath, e);
            return false;
        }
    }

    /**
     * Validates if a file size is within acceptable limits (default: 10 MB).
     * 
     * @param filePath the path to the file to validate
     * @return true if file size is valid, false otherwise
     */
    public static boolean isFileSizeValid(String filePath) {
        // Example: isFileSizeValid("/path/to/file.txt") => true/false
        try {
            return isFileSizeValid(filePath, MAX_FILE_SIZE);
        } catch (Exception e) {
            LOGGER.error("Unexpected error validating file size: {}", filePath, e);
            return false;
        }
    }

    /**
     * Validates if a file size is within specified limit.
     * 
     * @param filePath the path to the file to validate
     * @param maxSizeInBytes the maximum allowed file size in bytes
     * @return true if file size is valid and within limit, false otherwise
     * @throws IllegalArgumentException if maxSizeInBytes is negative
     */
    public static boolean isFileSizeValid(String filePath, long maxSizeInBytes) {
        // Example: isFileSizeValid("/path/to/file.txt", 5242880) => true/false (5 MB limit)
        try {
            if (filePath == null || filePath.trim().isEmpty()) {
                LOGGER.warn("File path is null or empty");
                return false;
            }

            if (maxSizeInBytes < 0) {
                throw new IllegalArgumentException("Maximum file size cannot be negative: " + maxSizeInBytes);
            }

            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                LOGGER.warn("File does not exist or is not a regular file: {}", filePath);
                return false;
            }

            long fileSize = file.length();
            boolean isValid = fileSize <= maxSizeInBytes;

            if (!isValid) {
                LOGGER.warn("File size ({} bytes) exceeds limit ({} bytes) for file: {}",
                    fileSize, maxSizeInBytes, filePath);
            } else {
                LOGGER.debug("File size validation passed for file: {} ({} bytes)", filePath, fileSize);
            }

            return isValid;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid argument in file size validation: {}", filePath, e);
            throw e;
        } catch (SecurityException e) {
            LOGGER.error("Security exception during file size validation: {}", filePath, e);
            return false;
        } catch (Exception e) {
            LOGGER.error("Unexpected error validating file size: {}", filePath, e);
            return false;
        }
    }

    // ============================= FILE TYPE VALIDATION =============================

    /**
     * Checks if a file has an allowed extension.
     * 
     * Validates if file extension matches one of the allowed extensions (case-insensitive).
     * 
     * @param fileName the name of the file to check
     * @param allowedExtensions array of allowed extensions (without leading dot)
     * @return true if file extension is in allowed list, false otherwise
     * @throws IllegalArgumentException if fileName or allowedExtensions is null or empty
     */
    public static boolean hasAllowedExtension(String fileName, String... allowedExtensions) {
        // Example: hasAllowedExtension("profile.jpg", "jpg", "png", "gif") => true
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new IllegalArgumentException("File name cannot be null or empty");
            }

            if (allowedExtensions == null || allowedExtensions.length == 0) {
                throw new IllegalArgumentException("Allowed extensions cannot be null or empty");
            }

            String extension = getFileExtension(fileName).toLowerCase();

            for (String allowed : allowedExtensions) {
                if (extension.equalsIgnoreCase(allowed)) {
                    LOGGER.debug("File extension validation passed for file: {}", fileName);
                    return true;
                }
            }

            LOGGER.warn("File extension '{}' not in allowed list for file: {}", extension, fileName);
            return false;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid argument in extension validation: {}", fileName, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error validating file extension: {}", fileName, e);
            return false;
        }
    }

    /**
     * Extracts the file extension from a file name.
     * 
     * Returns the extension without the leading dot. If no extension is found,
     * returns an empty string.
     * 
     * @param fileName the name of the file
     * @return the file extension without dot, or empty string if no extension
     * @throws IllegalArgumentException if fileName is null or empty
     */
    public static String getFileExtension(String fileName) {
        // Example: getFileExtension("document.pdf") => "pdf"
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new IllegalArgumentException("File name cannot be null or empty");
            }

            int lastDotIndex = fileName.lastIndexOf('.');

            if (lastDotIndex <= 0 || lastDotIndex == fileName.length() - 1) {
                LOGGER.debug("No extension found for file name: {}", fileName);
                return "";
            }

            String extension = fileName.substring(lastDotIndex + 1);
            LOGGER.debug("Extracted extension '{}' from file name: {}", extension, fileName);
            return extension;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid argument extracting extension from: {}", fileName, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error extracting file extension: {}", fileName, e);
            return "";
        }
    }

    // ============================= FILE OPERATIONS =============================

    /**
     * Safely creates a directory and all parent directories if they don't exist.
     * 
     * Creates the specified directory path recursively, similar to mkdir -p.
     * 
     * @param directoryPath the path to the directory to create
     * @return true if directory was created or already exists, false otherwise
     * @throws IllegalArgumentException if directoryPath is null or empty
     */
    public static boolean createDirectoryIfNotExists(String directoryPath) {
        // Example: createDirectoryIfNotExists("/path/to/directory") => true/false
        try {
            if (directoryPath == null || directoryPath.trim().isEmpty()) {
                throw new IllegalArgumentException("Directory path cannot be null or empty");
            }

            Path path = Paths.get(directoryPath);

            if (Files.notExists(path)) {
                Files.createDirectories(path);
                LOGGER.info("Directory created successfully: {}", directoryPath);
                return true;
            } else if (Files.isDirectory(path)) {
                LOGGER.debug("Directory already exists: {}", directoryPath);
                return true;
            } else {
                LOGGER.warn("Path exists but is not a directory: {}", directoryPath);
                return false;
            }

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid argument creating directory: {}", directoryPath, e);
            throw e;
        } catch (IOException e) {
            LOGGER.error("IO exception creating directory: {}", directoryPath, e);
            return false;
        } catch (SecurityException e) {
            LOGGER.error("Security exception creating directory: {}", directoryPath, e);
            return false;
        } catch (Exception e) {
            LOGGER.error("Unexpected error creating directory: {}", directoryPath, e);
            return false;
        }
    }

    /**
     * Safely deletes a file if it exists.
     * 
     * Attempts to delete the specified file. Does not throw exception if file
     * doesn't exist, but returns false.
     * 
     * @param filePath the path to the file to delete
     * @return true if file was deleted successfully, false otherwise
     * @throws IllegalArgumentException if filePath is null or empty
     */
    public static boolean deleteFileIfExists(String filePath) {
        // Example: deleteFileIfExists("/path/to/file.txt") => true/false
        try {
            if (filePath == null || filePath.trim().isEmpty()) {
                throw new IllegalArgumentException("File path cannot be null or empty");
            }

            Path path = Paths.get(filePath);
            boolean deleted = Files.deleteIfExists(path);

            if (deleted) {
                LOGGER.info("File deleted successfully: {}", filePath);
            } else {
                LOGGER.debug("File does not exist or could not be deleted: {}", filePath);
            }

            return deleted;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid argument deleting file: {}", filePath, e);
            throw e;
        } catch (IOException e) {
            LOGGER.error("IO exception deleting file: {}", filePath, e);
            return false;
        } catch (SecurityException e) {
            LOGGER.error("Security exception deleting file: {}", filePath, e);
            return false;
        } catch (Exception e) {
            LOGGER.error("Unexpected error deleting file: {}", filePath, e);
            return false;
        }
    }

    /**
     * Validates the entire file name for security and format compliance.
     * 
     * Checks: file name is not null/empty, contains only allowed characters,
     * and has a valid extension.
     * 
     * @param fileName the file name to validate
     * @return true if file name is valid, false otherwise
     * @throws IllegalArgumentException if fileName is null or empty
     */
    public static boolean isFileNameValid(String fileName) {
        // Example: isFileNameValid("my-document_123.pdf") => true
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new IllegalArgumentException("File name cannot be null or empty");
            }

            if (!VALID_FILENAME_PATTERN.matcher(fileName).matches()) {
                LOGGER.warn("File name contains invalid characters: {}", fileName);
                return false;
            }

            if (!fileName.contains(".")) {
                LOGGER.warn("File name has no extension: {}", fileName);
                return false;
            }

            LOGGER.debug("File name validation passed: {}", fileName);
            return true;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid argument validating file name: {}", fileName, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error validating file name: {}", fileName, e);
            return false;
        }
    }

    /**
     * Gets the file size in bytes.
     * 
     * @param filePath the path to the file
     * @return the file size in bytes, or -1 if file doesn't exist or error occurs
     * @throws IllegalArgumentException if filePath is null or empty
     */
    public static long getFileSize(String filePath) {
        // Example: getFileSize("/path/to/file.txt") => 1024
        try {
            if (filePath == null || filePath.trim().isEmpty()) {
                throw new IllegalArgumentException("File path cannot be null or empty");
            }

            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                long size = file.length();
                LOGGER.debug("File size retrieved for {}: {} bytes", filePath, size);
                return size;
            } else {
                LOGGER.warn("File does not exist or is not a regular file: {}", filePath);
                return -1;
            }

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid argument getting file size: {}", filePath, e);
            throw e;
        } catch (SecurityException e) {
            LOGGER.error("Security exception getting file size: {}", filePath, e);
            return -1;
        } catch (Exception e) {
            LOGGER.error("Unexpected error getting file size: {}", filePath, e);
            return -1;
        }
    }
}
