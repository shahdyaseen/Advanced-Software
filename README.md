<a name="readme-top"></a>
<div align="center">
     <br>
   <br>
  <img src="one.PNG" alt="Description of the image">
</div>
<div align="center">
  <br>
  <h1>Peer-to-Peer Rental Platform for Everyday Items</h1> &nbsp;
<div align="center">
  <p align="center">
    <a href="DEMO LINK">👾 View Demo</a>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="REPORT LINK">🐞 Report Bug </a>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </p>
     <br>
</div>
     
</div>
<div align="center">
  <img src="two.PNG" alt="Description of the image">
</div>
<br>
<br>




<a name="intro"></a>
## 🌟 About the Project
<strong>RentItOut</strong> is a peer-to-peer rental platform designed to make renting everyday items easy, reliable, and efficient. It connects people who have items to rent with those who need them, facilitating a convenient and cost-effective way to access products without purchasing them. The goal is to create a circular economy that encourages sharing and reduces the need for people to purchase items they only need occasionally.
<br>
<br>
<br>


<details>
  <summary><h2>💳 Table of Contents<h2\></summary>
  <ol>
    <li><a href="#intro">Introduction (What's RentItOut?)</a></li>
    <li><a href="#bw">Built With</a></li>
    <li><a href="#gs">Getting Started</a></li>
    <li><a href="#coref">Main Features</a></li>
    <li><a href="#roles">Roles</a></li>
    <li><a href="#API">API Documentation</a></li>
    <li><a href="#demo">Demo</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>
 <br>
 <br>
 <br>


<a name="bw"></a>
## 🔨 Built With
* [![SpringBoot][Spring-boot]][SpringURL] <br>An open-source Java framework for creating stand-alone, production-grade applications.
* [![MySQL][MySQL]][MySQLURL] <br>A reliable, open-source relational database management system commonly used for storing and managing data in web applications.
* [![Postman][Postman]][PostmanURL] <br>A collaboration platform for designing, testing, and documenting APIs.
* [![Github][Github]][GithubURL] <br>A web-based platform for version control and collaboration using Git.
<br>
<br>



<a name="gs"></a>
## 🚀 Getting Started
### ⚙️ Running the project
#### To get started with the project:
##### 1. Clone the repository:
> [![Github][Github]][wewe]
>
> sh
> git clone https://github.com/shahdyaseen/Advanced-Software.git
> 
##### 2. Install Dependencies
Make sure you have Maven installed. Run the following command to install the necessary dependencies:
>
> sh
> mvn clean install
> 
##### 3. Create The Database:
* Make sure MySQL is installed and running on your local machine.
* Create a new database for the project:
>
> sh
> CREATE DATABASE advanced_software;
> 
* Update the application.properties or application.yml file in the src/main/resources directory with your MySQL database credentials (username and password).
>
> sh
> spring.datasource.url=jdbc:mysql://localhost:3306/advanced_software
> spring.datasource.username=your_mysql_username
> spring.datasource.password=your_mysql_password
> 
##### 4. Run The Application:
>
> sh
> mvn spring-boot:run
> 
<br>
<br>
<br>



 <a name="coref"></a>

## 2. Main Features
### 🛠️ Item Listings for Rent
- <strong> Allow users to list items they rarely use, such as tools, electronics, sports equipment, and more. Listings can be organized into various categories to facilitate item discovery.</strong>This was done by implementing CRUD operations to create, retrieve, update, and delete item listing and developing endpoints to handle different categories, enabling efficient filtering and searching.
  <br>
  
### 📊 Rental Management and Pricing
- <strong> Manage rental durations, set flexible pricing models, and allow users to specify rental periods and conditions. </strong>Define pricing and duration models in the backend by using database tables to store rental rates and durations, and develop endpoints to handle rental availability, pricing rules, and extensions.
  <br>

### 🛡️ Trust, Safety, and Verification
- <strong> User verification, rating, and review systems to ensure safe and reliable transactions. Develop mechanisms for security deposits or damage protection. </strong>Integrate identity verification for users, potentially via third-party verification APIs. 
  <br>
  
### 🚚 Logistics: Delivery and Pickup
- <strong> Provide options for delivery or in-person pickup, with the potential for location-based matchmaking to facilitate exchanges. </strong>Integrate map-based location services to assist with pickup arrangements and delivery logistics.
  <br>

### 💰 Revenue Model and Insurance
- <strong> Define how the platform generates revenue, such as through service fees. Integrate insurance or damage protection. </strong>Calculate and implement platform fees or commissions for each transaction. 
  <br>

### ⭐ User Experience and Recommendations
- <strong> Enhance the user experience by offering personalized recommendations and a user-friendly interface.</strong> Leverage data on user interactions and item popularity to provide recommendations. 
  <br>
 <br>
 <br>
 <br>


 <a name="roles"></a>
# 👥 Roles:
- 👤 *Renter*: Users who rent out items.
- 👥 *Borrower*: Users who borrow items.
- 🔧 *Admin*: Users who manage the platform.
- 🏬 *Store Owner*: Users who own stores and rent items.
- 🏷️ *Supplier*: Users who supply items for rent.
- 💼 *Beneficiary*: Users who benefit from rented items.
  <br>
 <br>
 <br>





<a name="API"></a>
## 📝 API Documentation
Our API is fully documented and accessible through Postman, providing a detailed guide for all endpoints. You can view the latest documentation <a href="https://rental-app-6121.postman.co/workspace/Rental~dcb06ec3-4aed-4505-91d5-e5c923b168d2/documentation/39311362-637c5de3-149d-4b21-a1cd-f37e7494107f"><strong>here</strong></a>  once the backend is live. This documentation provides a clear overview of each endpoint, with details on request parameters, response structures, and practical examples to guide integration.
<br>
<br>
<br>
<br>


<a name="demo"></a>
## 📸 Demo
Get a firsthand look at our project in action! Watch the demo to explore its features and functionality.<a href="DEMO LINK">🚀 View Demo</a>
<br>
<br>
<br>
<br>



<a name="contact"></a>
## 📱 Contact
* Razan Dwekat - razan2003.dw@gmail.com
* Wafa'a Alshaikh Ibrahim - wafaj2017@gmail.com
* Haneed Alhaj - haneenradad2013@gmail.com
* Shahd Yaseen - shadthabit@gmail.com
<br>
  <p align="center"><a href="https://github.com/shahdyaseen/Advanced-Software/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=shahdyaseen/Advanced-Software" />
</a> </p>
 <p align="right">(<a href="#readme-top">⬆️</a>)</p>
 <br>
<br>
<br>
<br>



[MySQL]: https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white
[MySQLURL]: https://www.mysql.com/
[Spring-boot]: https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white
[SpringURL]: https://spring.io/projects/spring-boot
[GithubURL]: https://github.com/
[Postman]: https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white
[PostmanURL]: https://www.postman.com/
[wewe]: https://github.com/shahdyaseen/Advanced-Software.git
[JQuery-url]: https://jquery.com 
[Github]: https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white
