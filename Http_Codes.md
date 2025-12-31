### **1xx — Informational**

* **100 Continue** – The request headers were received; the client can send the body.
* **101 Switching Protocols** – The server is switching protocols (e.g., HTTP → WebSocket).

---

### **2xx — Success**

* **200 OK** – Request succeeded and returned data.
* **201 Created** – A new resource was successfully created.
* **202 Accepted** – Request accepted but processing is not finished yet.
* **204 No Content** – Request succeeded, but there is no response body.

---

### **3xx — Redirection**

* **301 Moved Permanently** – Resource has a new permanent URL.
* **302 Found** – Temporary redirect (often misused; still very common).
* **303 See Other** – Redirect after a POST, usually to a GET endpoint.
* **304 Not Modified** – Cached version is still valid.
* **307 Temporary Redirect** – Temporary redirect, method preserved.
* **308 Permanent Redirect** – Permanent redirect, method preserved.

---

### **4xx — Client Errors**

* **400 Bad Request** – Invalid or malformed request.
* **401 Unauthorized** – Authentication required or failed.
* **403 Forbidden** – Authenticated, but not allowed.
* **404 Not Found** – Resource does not exist.
* **405 Method Not Allowed** – HTTP method not supported on this endpoint.
* **409 Conflict** – Request conflicts with current state (e.g., duplicate resource).
* **410 Gone** – Resource permanently removed.
* **413 Payload Too Large** – Request body is too large.
* **415 Unsupported Media Type** – Content-Type not supported.
* **422 Unprocessable Entity** – Validation failed (very common in APIs).
* **429 Too Many Requests** – Rate limit exceeded.

---

### **5xx — Server Errors**

* **500 Internal Server Error** – Generic server failure.
* **501 Not Implemented** – Server does not support this functionality.
* **502 Bad Gateway** – Invalid response from upstream server.
* **503 Service Unavailable** – Server temporarily unavailable (overload, maintenance).
* **504 Gateway Timeout** – Upstream server did not respond in time.

---

### **Practical API usage summary**

* Use **200 / 201 / 204** for successful operations.
* Use **400 / 422** for client-side validation errors.
* Use **401 / 403** for auth-related failures (don’t confuse them).
* Use **404** when the resource truly does not exist.
* Use **409** for logical conflicts (duplicates, invalid state).
* Use **500+** only when the server is at fault.