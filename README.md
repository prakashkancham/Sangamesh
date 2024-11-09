# Java Application CI/CD Pipeline Documentation
---
I. **Setting Up and Running the Application Locally**

To set up and run the Java application locally:

1. **Clone the repository**:
   `git clone https://github.com/prakashkancham/sangamesh.git`
 
2. **Navigate to the project directory**:
   `cd sangamesh`
 
3. **Install dependencies**:
   Ensure that Maven is installed on your machine.
   Run:
  `mvn install`
 
5. **Run the application**:
   To build and run the application locally:
   `mvn clean package`
   `java -jar target/sangamesh.jar`
 
6. **Running Tests**:
   To execute the tests:
   `mvn test`
 
---

II. **CI/CD Pipeline Overview**

# Java CI/CD Pipeline with Docker and Ansible Deployment

This project features a CI/CD pipeline implemented in GitHub Actions to automate the build, test, and deployment process for a Java application. This pipeline handles Docker image creation, publishing to Docker Hub, and deployment to a remote EC2 instance using Ansible.

## Workflow Summary

The pipeline executes on every push or pull request targeting the `master` branch and includes the following key steps:

1. **Checkout Code**: Clones the repository to the GitHub Actions runner.
2. **Java Setup**: Configures Java 11 (Temurin distribution) on the runner environment.
3. **Build and Test**: Compiles the Java application using Maven and runs the unit tests.
4. **Docker Image Creation and Publishing**: Builds a Docker image, tags it, and pushes it to Docker Hub.
5. **Deployment to EC2 with Ansible**: Deploys the Docker container to a remote EC2 instance using Ansible playbooks.

## Prerequisites

To ensure the pipeline runs successfully, add the following secrets to your GitHub repository:

- **`GITHUB_TOKEN`**: Provided automatically by GitHub to interact with the repository.
- **`DOCKER_USERNAME`**: Your Docker Hub username.
- **`DOCKER_PASSWORD`**: Your Docker Hub password.

## Workflow Breakdown

### Trigger

The workflow is activated on any `push` or `pull request` targeting the `master` branch.

### Workflow Steps

#### 1. **Checkout Code**
   - Uses [actions/checkout](https://github.com/actions/checkout) to pull the latest code from the repository.

#### 2. **Set Up Java 11**
   - Configures the environment to use Java 11 (Temurin) via [actions/setup-java](https://github.com/actions/setup-java), ensuring compatibility with the Java application.

#### 3. **Build and Test with Maven**
   - Runs the Maven commands to clean, build, and test the application:
     - **Build**: `mvn clean package`
     - **Test**: `mvn test`

#### 4. **Docker Login, Build, Tag, and Push**
   - **Docker Login**: Authenticates with Docker Hub using the credentials stored in GitHub Secrets.
   - **Build Docker Image**: Constructs the Docker image for the application with `docker build -t app:latest .`.
   - **Tag Docker Image**: Tags the Docker image as `DOCKER_USERNAME/app:latest`.
   - **Push Docker Image**: Pushes the image to Docker Hub for external access and deployment.


## Setup and Usage

1. **Configure Secrets**: Add the required secrets (`GITHUB_TOKEN`, `DOCKER_USERNAME`, `DOCKER_PASSWORD`, `EC2_PRIVATE_KEY`, and `EC2_KNOWN_HOSTS`) to your GitHub repository.
2. **Ansible Setup**: Ensure `inventory` and `playbook.yml` files are appropriately set up to match your EC2 instance and deployment configuration.
3. **Docker Image Management**: Modify the image name or tag as necessary for your Docker Hub repository.

### Commands in the Workflow

- **Docker Login**: `docker login -u DOCKER_USERNAME --password-stdin`
- **Build Docker Image**: `docker build -t app:latest .`
- **Tag Docker Image**: `docker tag app:latest DOCKER_USERNAME/app:latest`
- **Push Docker Image**: `docker push DOCKER_USERNAME/app:latest`
- **Deploy Using Ansible**: `ansible-playbook -i inventory playbook.yml`

---

III. **Dockerfile and Ansible Playbooks**

### Dockerfile

The `Dockerfile` in the project specifies how the Java application is containerized. Here’s the Dockerfile:

```
FROM maven:3.8.5-openjdk-11 AS maven_build
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM eclipse-temurin:11
EXPOSE 8080
CMD java -jar /data/hello-world-0.1.0.jar
COPY --from=maven_build /tmp/target/hello-world-0.1.0.jar /data/hello-world-0.1.0.jar
```

This file:
- Copies the packaged `.jar` file from the target directory into the container.
- Runs the Java application inside the container.

### Ansible Playbooks

The Ansible playbooks automate the deployment of the Docker container to a remote EC2 instance. They typically contain the following steps:

1. **Install Docker**: Ensures Docker is installed on the EC2 instance.
2. **Pull Docker Image**: Pulls the image from Docker Hub.
3. **Run Docker Container**: Deploys the application inside a Docker container.

Here is the playbook (`playbook.yml`):
```
- hosts: ec2_instance
  become: yes
  vars:
    ansible_python_interpreter: /usr/bin/python3.9
  tasks:
    - name: Ensure Docker is running
      service:
        name: docker
        state: started
        enabled: yes

    - name: Docker image pull
      command: docker pull prakashkancham/app:latest
      
    - name: Run Docker container
      command: docker run -itd -p 8080:8080 app:latest
```
---

IV. **Challenges Faced and How They Were Overcome**

### Challenge 1: **Handling Dependencies**

- **Issue**: Ensuring that all dependencies were correctly installed for the application to run smoothly in both local and CI environments.
- **Solution**: The use of Maven to handle project dependencies and ensure consistency across environments.

### Challenge 2: **Docker Build and Push**

- **Issue**: Docker image size and efficient deployment process.
- **Solution**: Optimized the Dockerfile to reduce the image size by using a slim base image and only copying necessary files into the container.

### Challenge 3: **EC2 Deployment**

- **Issue**: Securely managing SSH keys for EC2 access within GitHub Actions.
- **Solution**: Using GitHub Secrets to store private keys and known hosts information securely, and incorporating them in the CI/CD workflow for automated deployment.
---
