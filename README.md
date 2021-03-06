# typetalk

A Clojure library designed to ... well, that part is up to you.

## Usage



## API

### get-access-token

```clojure
(get-access-token client-id client-secret scope)
```


### authorization-url

```clojure
(authorization-url client-id redirect-uri scope)
```


### refresh-access-token

```clojure
(refresh-access-token client_id client_secret access-token)
```


### get-profile

```clojure
(get-profile access-token)
```


### get-topics

```clojure
(get-topics access-token)
```


### get-posts

```clojure
(get-posts access-token topic)
```


### create-post

```clojure
(create-post access-token topic message)
```


### get-post

```clojure
(get-post access-token post)
 ```
 

### delete-post

```clojure
(delete-post access-token post)
 ```
 

### create-like

```clojure
(create-like access-token post)
```


### delete-like

```clojure
(delete-like access-token post)
```


### create-favorite

```clojure
(create-favorite access-token topic)
```


### delete-favorite

```clojure
(delete-favorite access-token topic)
```


### get-notifications

```clojure
(get-notifications access-token)
```
 

### open-notifications

```clojure
(open-notifications access-token topic)
```


### mark-topic-as-read

```clojure
(mark-topic-as-read access-token topic)
```

### mark-post-as-read

```clojure
(mark-post-as-read access-token post)
```


### get-mentions

```clojure
(get-mentions access-token & options)
```


### mark-mention-as-read

```clojure
(mark-mention-as-read access-token mention)
```


## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
