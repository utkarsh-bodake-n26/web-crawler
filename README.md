
# Web Crawler

This service is used to crawl a given url and generate a sitemap out of it.

-----

# Approach
##### What we want to achieve:
- We want to crawl a web page which can have many child links which in turn can have 
many sub-child links and so on. Also we do not want to process any URL more than once.

##### Solution
- This seems like a classic graph problem where a graph node can have many links.
- So to create a sitemap we have traversed the web pages using `Depth First Traversal` using `Stack` data structure.
- Also we have used `Set` data structure to gurantee that each URL is visited only once.
- Note: This can alternatively be solved using recursion too.
----
# Entities
### PageNode Entity
```
- Represents node in a graph.
{
	"url": "http://crowdfireapp.com",
	"title": "Crowdfireapp - Social Media Management, Simplified",
	"nodes": [
	    {... Child PageNode Entity...},
	    ...
	]
}
Where:
- url: URL of the web page
- title: Title cof the web page
- nodes: List of child Pagenodes which represent sub-links of the given URL.
```

# APIs
- Structure: ```GET /api/v1/crawler```
- Query params:
    ```
    url="http://crowdfireapp.com"
    ```
- Example using cURL request:
    ```
	  curl -X GET 'http://localhost:8080/api/v1/crawler?url=http://crowdfireapp.com'
    ```
    ```
    Success response
    
    HTTP status code: 200 OK - in case of success
    {
    	"url": "http://crowdfireapp.com",
    	"title": "Crowdfireapp - Social Media Management, Simplified",
    	"nodes": [
    	    {... Child PageNode Entity...},
    	    ...
    	]
    }
    ```

    ```
    Failure responses
    
    Status code: 400
    Incase mandatory query param missing or invalid format of url provided
    ```

## Running of application web server locally
1. Go to base directory of the project `cd web-crawler` and then run ``mvn spring-boot:run`` which will start the 
application Tomcat web 
server listening on port 8080.
2. All API's will be accessible with Base URL `http://localhost:8080/`.
3. To quick test run: `curl -X GET 'http://localhost:8080/api/v1/crawler?url=http://mkbhd.com/'`


## Test cases

- Unit Test cases have been added for the crawl module to check the functionality at the grass root level.
- Run tests: `mvn test`

## Environment Setup
Install the following:
 - Java JDK 1.8
 - Apache Maven 3.3+
 - Git

## Build
* Clone this repository: `git@github.com:utkarsh-bodake-n26/web-crawler.git`
* Change directory to: `cd web-crawler`

## JAR
Build project: `mvn clean install`

## Artifacts
1. Executable jar file: `./web-crawler/target/web-crawler-${version}-SNAPSHOT.jar`

-----
# Known Limitations
 - Current implementation does not have any mechanism to limit the depth or max number of web pages which can be 
 crawled. This can be easily implemented by adding extra input to the API.
 
