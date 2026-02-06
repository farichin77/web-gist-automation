# Web Automation Gist

## 📋 Project Overview

Automated testing framework for GitHub Gist functionality using Selenium WebDriver and TestNG. This project provides comprehensive test coverage for Gist creation, updating, deletion, and listing operations.

## 🚀 Features

- **Multi-browser Support**: Chrome, Firefox, Edge
- **TestNG Framework**: Structured test execution with parallel testing
- **ExtentReports**: Detailed HTML test reports with screenshots
- **GitHub Actions CI/CD**: Automated testing and report generation
- **Environment Variable Security**: Secure credential management
- **Screenshot Capture**: Automatic screenshots on test failures

## 🛠️ Technology Stack

- **Java 17**
- **Selenium WebDriver 4.27.0**
- **TestNG 7.10.2**
- **ExtentReports 5.1.1**
- **WebDriverManager 5.9.2**
- **Gradle 8.14**

## 📁 Project Structure

```
web-automation-gits/
├── src/
│   ├── main/java/gist/
│   │   ├── core/
│   │   │   ├── BasePage.java          # Base page functionality
│   │   │   ├── BaseTest.java          # Base test setup
│   │   │   └── DriverManager.java     # WebDriver management
│   │   └── pages/
│   │       ├── LoginPage.java          # GitHub login page
│   │       ├── GistPage.java          # Gist operations page
│   │       └── ...
│   └── test/java/gist/
│       ├── tests/
│       │   ├── CreateGistTest.java    # Gist creation tests
│       │   ├── UpdateGistTest.java    # Gist update tests
│       │   ├── DeleteGistTest.java    # Gist deletion tests
│       │   └── SeeListOfGist.java      # Gist listing tests
│       └── listeners/
│           └── TestListener.java      # Test execution listener
├── src/test/resources/
│   └── testng.xml                     # TestNG configuration
├── test-reports/                      # Generated test reports
├── .github/workflows/
│   └── ci.yml                         # GitHub Actions workflow
├── build.gradle                       # Gradle build configuration
└── README.md                          # This file
```

## 🔧 Setup Instructions

### Prerequisites

- Java 17 or higher
- Gradle 8.14+
- Chrome/Firefox/Edge browsers
- GitHub account (for testing)

### Local Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/farichin77/web-gist-automation.git
   cd web-gist-automation
   ```

2. **Create environment file**
   ```bash
   cp .env.example .env
   ```

3. **Configure environment variables**
   ```bash
   # .env file
   BASE_URL=https://github.com
   GITHUB_USERNAME=your_github_username
   GITHUB_PASSWORD=your_github_password
   ```

4. **Run tests**
   ```bash
   ./gradlew test
   ```

### GitHub Secrets Configuration

For CI/CD pipeline, configure these secrets in GitHub repository settings:

1. Go to **Settings** → **Secrets and variables** → **Actions**
2. Add the following secrets:
   ```
   BASE_URL=https://github.com
   TEST_USERNAME=your_github_username
   TEST_PASSWORD=your_github_password
   ```

## 🧪 Test Execution

### Running All Tests
```bash
./gradlew test
```

### Running Specific Test Classes
```bash
./gradlew test --tests "gist.tests.CreateGistTest"
```

### Running with Specific Browser
```bash
./gradlew test -Dbrowser=chrome
```

### Parallel Test Execution
Tests are configured to run in parallel across multiple browsers as defined in `testng.xml`.

## 📊 Test Reports

After test execution, reports are generated in:

- **HTML Reports**: `build/reports/tests/test/index.html`
- **ExtentReports**: `test-reports/ExtentReport_*.html`
- **Screenshots**: `test-reports/screenshots/`
- **TestNG XML**: `build/test-results/test/`

### GitHub Actions Artifacts

When running on GitHub Actions, all test reports are automatically uploaded as artifacts:
- `test-reports-{timestamp}` - Complete test reports
- `extent-reports-{timestamp}` - ExtentReports HTML
- `screenshots-{timestamp}` - Failure screenshots

## 🔄 CI/CD Pipeline

### Automated Workflow Triggers

- **Push** to main/master/develop branches
- **Pull Request** to main/master
- **Manual dispatch** via GitHub Actions UI

### Workflow Steps

1. **Code Checkout** - Fetch latest code
2. **Java Setup** - Configure Java 17 environment
3. **Dependency Cache** - Cache Gradle dependencies
4. **Test Execution** - Run automated tests with environment variables
5. **Report Generation** - Create comprehensive test reports
6. **Artifact Upload** - Upload all reports and screenshots
7. **Download Summary** - Provide clear download instructions

## 🐛 Troubleshooting

### Common Issues

1. **Browser Driver Issues**
   - Ensure WebDriverManager is properly configured
   - Check browser compatibility versions

2. **Environment Variable Issues**
   - Verify `.env` file exists and is properly configured
   - Check GitHub Secrets are correctly set

3. **Test Failures**
   - Check test reports for detailed error messages
   - Verify GitHub credentials are valid
   - Ensure 2FA is handled if enabled

### Debug Mode

Run tests with additional logging:
```bash
./gradlew test --info --stacktrace
```

## 📝 Test Cases

### Covered Scenarios

1. **Gist Creation**
   - Create public gist
   - Create private gist
   - Create gist with description
   - Create gist with file content

2. **Gist Updates**
   - Update gist description
   - Update file content
   - Add new files to gist
   - Delete files from gist

3. **Gist Deletion**
   - Delete single gist
   - Delete multiple gists

4. **Gist Listing**
   - View public gists
   - View private gists
   - Search gists
   - Filter gists by date

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

- **Author**: Farichin
- **Email**: [your-email@example.com]
- **GitHub**: [@farichin77](https://github.com/farichin77)

## 🙏 Acknowledgments

- [Selenium WebDriver](https://www.selenium.dev/) - Browser automation
- [TestNG](https://testng.org/) - Testing framework
- [ExtentReports](https://www.extentreports.com/) - Reporting solution
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) - Driver management
