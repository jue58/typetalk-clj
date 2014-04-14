# typetalk

A Clojure library designed to ... well, that part is up to you.

## Usage



## API

### (get-access-token client-id client-secret scope)

### (authorization-url client-id redirect-uri scope)

### (refresh-access-token client_id client_secret access-token)

### (get-profile access-token)

### (get-topics access-token)

### (get-posts access-token topic)

### (create-post access_token topic message)

### (get-post access-token post)

### (delete-post access-token post)

### (create-like access-token post)

### (delete-like access-token post)

### (create-favorite access_token topic)

### (delete-favorite access_token topic)

### (get-notifications access-token)

### (open-notifications access_token topic)

### (mark-topic-as-read access_token topic)

### (mark-post-as-read access_token post)

### (get-mentions access_token & options)

### (mark-mention-as-read access_token mention)

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
