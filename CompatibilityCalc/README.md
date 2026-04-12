## Setup

### Requirements
- Java 17+
- MySQL 8.0+
- IntelliJ IDEA

### First time setup
1. Clone the repo
2. Open in IntelliJ — Maven downloads the MySQL
   connector automatically
3. In MySQL Workbench, run `sql/compatibility_db.sql`
4. Copy `config.example.properties` → `config.properties`
5. Fill in your MySQL credentials in `config.properties`
6. Run `Main.java`

### Note
`config.properties` is gitignored.
Never commit it. Each person keeps their own local copy.