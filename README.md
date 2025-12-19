# Certification Quiz App

A modern desktop application for taking certification exams in offline mode. Built with **JavaFX** using the **MVC (Model-View-Controller)** architectural pattern.

## Quick Start

### Requirements
- **JDK 21** or higher  
- **Maven 3.8+**

### Run from the Terminal
```bash
mvn clean javafx:run
````

### Question File Format

The application parses questions from a `.txt` file in the following format:

```text
Q: Your question text?
- Answer option 1
- Answer option 2
- Answer option 3
A: 0, 2  #(indices of correct answers, comma-separated)
```

---

## Building an Executable

### Creating a Portable Version (No Installation Required)

To generate an application folder that can be run on any PC without a preinstalled Java runtime:

```bash
mvn clean javafx:jlink
```

The result will be located in:  
`target/certification-quiz-app/bin`

### Creating an Installer (.exe)

Requires the installed [WiX Toolset](https://wixtoolset.org/).

Create a portable

```bash
mvn clean javafx:jlink
```

Create .exe file 

```bash
jpackage --type exe \
  --name "CertificationQuiz" \
  --module com.leverx.certificationquizapp/com.leverx.certificationquizapp.CertificationQuizApp \
  --runtime-image target/certification-quiz-app \
  --dest target/installer
```

You can find .exe in `target/installer`
