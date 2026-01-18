# 📚 Library Management System

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.java.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

A comprehensive desktop application built with Java Swing for efficient library management. This system streamlines the process of managing books, members, and loan transactions with an intuitive graphical interface and robust data handling.

> 🎓 **Academic Project** - Developed as part of the Bachelor of Science (ND) in Computer Software Engineering program at Lufem College of Technology, Agege, Lagos.

## ✨ Features

### 📖 Book Management
- Add, edit, and delete books with validation
- Automatic book ID generation
- ISBN validation (10/13 digits)
- Real-time availability tracking
- Search books by title, author, or ISBN
- Export book catalog to CSV

### 👤 Member Management
- Register and manage library members
- Member status tracking (Active/Inactive)
- Email validation
- Unique member ID system
- Member search functionality

### 📋 Loan Management
- Issue books to active members
- Automatic due date calculation (14-day period)
- Return book processing
- Overdue loan detection and alerts
- Complete loan history tracking
- Referential integrity (prevents deletion of items with active loans)

### 🎯 Additional Features
- **Interactive Dashboard** - Real-time statistics and quick actions
- **Global Search** - Search across books, members, and loans simultaneously
- **Undo/Redo** - Reverse accidental book operations
- **CSV Data Storage** - Portable, human-readable data files
- **Report Generation** - Print book catalogs and loan reports
- **Keyboard Shortcuts** - Efficient navigation (Ctrl+N, Ctrl+F, Ctrl+Z, etc.)
- **Data Validation** - Comprehensive input validation and error handling

## 📸 Screenshots

### Dashboard
<img width="1366" height="768" alt="Dashboard View" src="https://github.com/user-attachments/assets/97c748d1-0d53-429c-a352-4a0fcb24a4fa" />

*Main dashboard showing library statistics and quick actions*

### Book Management
<!-- Add your book management screenshot here -->

### Loan Processing
<!-- Add your loan processing screenshot here -->

## 🚀 Installation & Setup

### Prerequisites
- **Java JDK 8 or higher** ([Download here](https://adoptium.net/))
- **Git** (optional, for cloning)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/alongesodig/Library-Management-System.git
   cd Library-Management-System
   ```

2. **Compile the application**
   ```bash
   # Create bin directory
   mkdir bin
   
   # Compile all Java files
   javac -d bin -sourcepath src src/com/lms/Main.java src/com/lms/model/*.java src/com/lms/service/*.java src/com/lms/ui/*.java
   ```

3. **Run the application**
   ```bash
   java -cp bin com.lms.Main
   ```

### Alternative: Using an IDE

#### IntelliJ IDEA / Eclipse / NetBeans
1. Import the project as a Java application
2. Set the source folder to `src`
3. Run `Main.java` located in `src/com/lms/`

#### Visual Studio Code
1. Install the "Extension Pack for Java"
2. Open the project folder
3. Navigate to `src/com/lms/Main.java`
4. Right-click and select "Run Java"

## 📁 Project Structure

```
Library-Management-System/
│
── com/
│       └── lms/
│           ├── service/            # Business logic
│           │   ├── LibraryManager.java
│           │   └── CSVFileHandler.java
│           │
│           ├── ui/                 # GUI components
│           │   ├── LibraryAppGUI.java
│           │   ├── AddBookDialog.java
│           │   ├── AddMemberDialog.java
│           │   ├── AddLoanDialog.java
│           │   └── SearchDialog.java
│           │
│           └── Main.java           # Application entry point
│
├── data/                           # CSV data files
│   ├── books.csv
│   ├── members.csv
│   └── loans.csv
│
└── README.md
```

## 💻 Usage Guide

### Adding a Book
1. Navigate to **Books** tab
2. Click **Add Book** button (or press `Ctrl+N`)
3. Enter book details (Title, Author, ISBN)
4. Click **Add Book**

### Registering a Member
1. Go to **Members** tab
2. Click **Register Member**
3. Enter member ID, name, and email
4. Click **Register Member**

### Issuing a Book
1. Open **Loans** tab
2. Click **Issue Book**
3. Select member and available book from dropdowns
4. Click **Issue Book** (due date is automatically set to 14 days)

### Returning a Book
1. In **Loans** tab, select the active loan
2. Click **Return Book**
3. The book becomes available immediately

### Global Search
- Press `Ctrl+F` or go to **Tools → Global Search**
- Enter search query
- View results categorized by Books, Members, and Loans

## ⌨️ Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Ctrl+N` | Add new book |
| `Ctrl+F` | Global search |
| `Ctrl+Z` | Undo last book operation |
| `Ctrl+Y` | Redo book operation |
| `F5` | Refresh data |
| `Alt+F4` | Exit application |

## 🗄️ Data Storage

Data is stored in CSV format in the `data/` directory:

- **books.csv** - Book inventory
- **members.csv** - Member records
- **loans.csv** - Loan transactions

### Data Backup
Simply copy the `data/` folder to create a backup. To restore, replace the folder with your backup.

## 🛠️ Technologies Used

- **Language**: Java 8+
- **GUI Framework**: Java Swing
- **Data Storage**: CSV (Comma-Separated Values)
- **Architecture**: MVC (Model-View-Controller)
- **Build System**: Standard Java Compilation

## 📊 System Requirements

### Minimum Requirements
- **OS**: Windows 7/10/11, macOS 10.12+, or Linux
- **RAM**: 2 GB
- **Storage**: 500 MB available space
- **Display**: 1024 × 768 resolution

### Recommended Requirements
- **RAM**: 4 GB or higher
- **Display**: 1280 × 1024 or higher resolution

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java naming conventions
- Add comments for complex logic
- Test thoroughly before submitting PR
- Update documentation for new features

## 🐛 Known Issues & Limitations

- Single-user desktop application (no multi-user support)
- CSV files may have performance issues with 50,000+ records
- No built-in backup scheduler (manual backup required)
- Limited to English language interface

## 🔮 Future Enhancements

- [ ] Multi-user support with user authentication
- [ ] Database integration (MySQL/PostgreSQL)
- [ ] Barcode scanning support
- [ ] Email notifications for due dates
- [ ] Fine calculation and payment tracking
- [ ] Web-based interface
- [ ] Mobile app companion
- [ ] Advanced reporting and analytics
- [ ] Book reservation system
- [ ] Integration with library catalog APIs

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Alonge Sodiq Ayomide**
- Matric No: 202303004
- Department: Computer Software Engineer
- Institution: Lufem College of Technology, Agege, Lagos
- GitHub: [@AlongeSodig](https://github.com/Ayokule)

**Supervisor**: Mr. Agbolade Odunsami

## 🙏 Acknowledgments

- Lufem College of Technology for academic support
- Mr. Agbolade Odunsami for supervision and guidance
- Department of Computer Software Engineering
- Open source community for Java Swing resources

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/Ayokule/Library-Management-System/issues) page
2. Create a new issue with detailed description
3. Include error messages and screenshots if applicable

## 📈 Project Statistics

![GitHub repo size](https://img.shields.io/github/repo-size/alongesodig/Library-Management-System)
![GitHub last commit](https://img.shields.io/github/last-commit/Ayokule/Library-Management-System)
![GitHub issues](https://img.shields.io/github/issues/alongesodig/Library-Management-System)

---

<div align="center">

**⭐ Star this repository if you found it helpful!**

Made with ❤️ by [Alonge Sodiq Ayomide](https://github.com/Ayokule)

</div>
