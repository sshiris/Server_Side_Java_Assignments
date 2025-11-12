# Employee Management System - External Tomcat Setup

## Project Overview

This project consists of:
- **Server**: Spring Boot REST API deployed on external Tomcat
- **Client**: Spring Boot CLI application that connects to the server
- **Features**: Employee management with image upload/download functionality

---

## System Requirements

- Java 21 or higher
- Maven 3.9.11 or higher
- Apache Tomcat 11.0.11 (downloaded separately)
- macOS (or Linux/Windows with similar commands)

---

## Initial Setup (One Time Only)

### Step 1: Create Upload Directory

```bash
mkdir -p ~/tomcat-uploads
```

This folder will store all uploaded images.

### Step 2: Build the Server

```bash
cd /Users/iris/SSAs/SpringBoot/ssa-IV-server
mvn clean package
```

This creates: `target/ssa-IV-server-0.0.1-SNAPSHOT.war`

### Step 3: Deploy to Tomcat

```bash
# Copy the .war file to Tomcat webapps folder
cp /Users/iris/SSAs/SpringBoot/ssa-IV-server/target/ssa-IV-server-0.0.1-SNAPSHOT.war \
   ~/Downloads/apache-tomcat-11.0.11/webapps/ssa-IV-server.war
```

Verify deployment:
```bash
ls -la ~/Downloads/apache-tomcat-11.0.11/webapps/ | grep ssa-IV-server
```

---

## Running the Application

### Terminal 1: Start External Tomcat Server

```bash
cd ~/Downloads/apache-tomcat-11.0.11/bin

# Set environment variable for file uploads
export SSA_UPLOAD_DIR=~/tomcat-uploads

# Start Tomcat
./catalina.sh start

# You should see: "Tomcat started."
# Keep this terminal open!
```

### Verify Server is Running

```bash
# In a new terminal, test the server
curl http://localhost:8080/ssa-IV-server/employees

# You should see the employee list in JSON format
```

### Terminal 2: Start Client Application

```bash
cd /Users/iris/SSAs/SpringBoot/ssa-IV-client

# Run the client
mvn spring-boot:run -Dspring-boot.run.main-class=ssa.ssa_IV_client.Client

# You should see the menu:
# === Employee Management System ===
# 1. Show All Employees
# 2. Add Employee
# ... (etc)
```

---

## Using the Application

### Menu Options

```
1. Show All Employees      - Display all employees
2. Add Employee            - Add a new employee
3. Update Employee         - Update existing employee info
4. Delete Employee         - Remove an employee
5. Upload Image            - Upload image from anywhere on your PC
6. Download Image          - Download image to client-downloads folder
7. List Available Files    - Show available files
8. Quit                    - Exit application
```

### Example: Upload an Image

```
Menu: Select "5. Upload Image"
   ↓
"Enter image file path: /Users/iris/Desktop/photo.jpg"
   ↓
"File uploaded successfully!"
   ↓
File is now stored at: ~/tomcat-uploads/photo.jpg
```

### Example: Add Employee with Image

```
Menu: Select "2. Add Employee"
   ↓
Enter ID: 3
Enter Name: John
Enter Job: Manager
Enter Salary: 9000
Enter Image Filename (e.g., iris1.jpg): photo.jpg
   ↓
"John new employee added successfully"
```

---

## Stopping the Application

### Stop Tomcat

```bash
~/Downloads/apache-tomcat-11.0.11/bin/catalina.sh stop
```

### Stop Client

Press `8. Quit` in the client menu, or press `Ctrl+C` in Terminal 2

---

## File Locations

| Component | Location |
|-----------|----------|
| Tomcat Installation | `~/Downloads/apache-tomcat-11.0.11/` |
| Uploaded Images | `~/tomcat-uploads/` |
| Server WAR File | `~/Downloads/apache-tomcat-11.0.11/webapps/ssa-IV-server.war` |
| Server Code | `/Users/iris/SSAs/SpringBoot/ssa-IV-server/` |
| Client Code | `/Users/iris/SSAs/SpringBoot/ssa-IV-client/` |
| Tomcat Logs | `~/Downloads/apache-tomcat-11.0.11/logs/catalina.out` |

---

## Troubleshooting

### Issue: Tomcat won't start

**Check logs:**
```bash
tail -f ~/Downloads/apache-tomcat-11.0.11/logs/catalina.out
```

**Possible causes:**
- Port 8080 already in use
- Java not installed
- Environment variable not set

### Issue: Client can't connect to server

**Verify server is running:**
```bash
curl http://localhost:8080/ssa-IV-server/employees
```

**Check firewall:**
```bash
# Port 8080 should be accessible
lsof -i :8080
```

### Issue: Uploaded images not found

**Check environment variable was set:**
```bash
echo $SSA_UPLOAD_DIR
# Should output: ~/tomcat-uploads
```

**Verify folder exists:**
```bash
ls -la ~/tomcat-uploads/
```

### Issue: File upload fails

**Check file path is correct:**
```bash
# Make sure the file exists
ls -la /path/to/your/file.jpg
```

**Check file permissions:**
```bash
# tomcat-uploads should be writable
chmod 755 ~/tomcat-uploads
```

---

## Architecture

```
Your Laptop
│
├─ Terminal 1: External Tomcat (Port 8080)
│  └─ Runs: ssa-IV-server.war
│     ├─ Stores files at: ~/tomcat-uploads/
│     └─ Serves REST API: http://localhost:8080/ssa-IV-server/employees
│
├─ Terminal 2: Spring Boot Client
│  └─ Connects to: http://localhost:8080/ssa-IV-server/employees
│     ├─ Takes user input
│     └─ Uploads/downloads files
│
└─ File Storage: ~/tomcat-uploads/
   ├─ iris1.jpg
   ├─ photo.jpg
   └─ (all uploaded images)
```

---

## Key Differences: Embedded vs External Tomcat

| Aspect | Embedded (Eclipse) | External (Downloaded) |
|--------|-------------------|----------------------|
| **Start Method** | Eclipse GUI | Terminal: `./catalina.sh start` |
| **Location** | Inside Eclipse | `~/Downloads/apache-tomcat-11.0.11/` |
| **Configuration** | Automatic | Manual (server.xml) |
| **Deployment** | Automatic | Manual (.war copy) |
| **Port** | 8080 | 8080 (or change in server.xml) |
| **Logs** | Eclipse console | `~/Downloads/apache-tomcat-11.0.11/logs/` |

---

## Advanced: Changing the Port

If you want to run on a different port (e.g., 9090):

### 1. Edit server.xml

```bash
nano ~/Downloads/apache-tomcat-11.0.11/conf/server.xml
```

Find line with `port="8080"` and change to `port="9090"`

### 2. Restart Tomcat

```bash
~/Downloads/apache-tomcat-11.0.11/bin/catalina.sh stop
sleep 2
cd ~/Downloads/apache-tomcat-11.0.11/bin
export SSA_UPLOAD_DIR=~/tomcat-uploads
./catalina.sh start
```

### 3. Update Client

Update `Client.java`:
```java
private static final String BASE_URI = "http://localhost:9090/ssa-IV-server/employees";
```

---

## Notes for Production Deployment

When deploying to a real server:

1. **Set environment variable on server:**
   ```bash
   export SSA_UPLOAD_DIR=/var/www/uploads
   ```

2. **Update client BASE_URI:**
   ```java
   private static final String BASE_URI = "http://your-server.com:8080/ssa-IV-server/employees";
   ```

3. **Ensure firewall allows port 8080**

4. **Use proper SSL/HTTPS in production**

---

## Support

For issues or questions, check:
- Tomcat logs: `~/Downloads/apache-tomcat-11.0.11/logs/catalina.out`
- Server status: `curl http://localhost:8080/ssa-IV-server/employees`
- Environment variable: `echo $SSA_UPLOAD_DIR`

---

---

## pom.xml Configuration for External Tomcat

### Server pom.xml

The server requires specific Maven configuration to deploy on external Tomcat:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.7</version>
        <relativePath/>
    </parent>
    
    <groupId>ssa</groupId>
    <artifactId>ssa-IV-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>war</packaging>  <!-- ✅ IMPORTANT: war (not jar) -->
    
    <name>ssa-IV-server</name>
    <description>Employee Management Server</description>
    
    <properties>
        <java.version>21</java.version>
    </properties>
    
    <dependencies>
        <!-- Spring Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- ✅ IMPORTANT: External Tomcat (provided by Tomcat) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
    
</project>
```

### Key Configuration Points

1. **`<packaging>war</packaging>`**
   - Creates a `.war` file (Web Application Archive)
   - Required for external Tomcat deployment
   - Without this, it creates `.jar` file (embedded Tomcat only)

2. **`spring-boot-starter-tomcat` with `<scope>provided</scope>`**
   - Tells Maven: External Tomcat will provide Tomcat libraries
   - Don't include Tomcat in the package
   - External Tomcat supplies Tomcat at runtime

### Client pom.xml

The client also needs specific configuration:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.7</version>
        <relativePath/>
    </parent>
    
    <groupId>ssa</groupId>
    <artifactId>ssa-IV-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>  <!-- ✅ jar (for standalone CLI) -->
    
    <name>ssa-IV-client</name>
    <description>Employee Management Client</description>
    
    <properties>
        <java.version>21</java.version>
        <start-class>ssa.ssa_IV_client.Client</start-class>
    </properties>
    
    <dependencies>
        <!-- Spring WebFlux (for async HTTP client) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
    
</project>
```

### Client pom.xml Key Points

1. **`<packaging>jar</packaging>`**
   - Client is a standalone CLI application
   - Creates executable `.jar` file
   - Different from server (which is `.war`)

2. **`<start-class>ssa.ssa_IV_client.Client</start-class>`**
   - Specifies which class contains the main method
   - Allows running: `mvn spring-boot:run`
   - Without this, Maven can't find which main class to run

3. **`spring-boot-starter-webflux`**
   - Provides WebClient for async HTTP requests
   - Used for uploading/downloading files
   - Better for streaming large files

---

## Building and Deploying

### Build Server (Creates .war)

```bash
cd /Users/iris/SSAs/SpringBoot/ssa-IV-server
mvn clean package
```

Output: `target/ssa-IV-server-0.0.1-SNAPSHOT.war`

### Deploy Server

```bash
cp target/ssa-IV-server-0.0.1-SNAPSHOT.war \
   ~/Downloads/apache-tomcat-11.0.11/webapps/ssa-IV-server.war
```

### Run Client (jar)

```bash
cd /Users/iris/SSAs/SpringBoot/ssa-IV-client
mvn spring-boot:run -Dspring-boot.run.main-class=ssa.ssa_IV_client.Client
```

---

## Differences: .war vs .jar

| Aspect | .war (Server) | .jar (Client) |
|--------|---------------|---------------|
| **Type** | Web Application Archive | Java Archive |
| **Deployment** | External Tomcat | Standalone execution |
| **Packaging** | `<packaging>war</packaging>` | `<packaging>jar</packaging>` |
| **Tomcat** | `<scope>provided</scope>` | Not needed |
| **Execution** | `./catalina.sh start` | `java -jar` or `mvn spring-boot:run` |
| **Use Case** | Server running 24/7 | Client CLI tool |