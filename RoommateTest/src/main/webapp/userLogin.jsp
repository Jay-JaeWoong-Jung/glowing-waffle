<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1"><link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link href="signup.css" rel="stylesheet">


    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha256-eZrrJcwDc/3uDhsdt61sL2oOBY362qM3lon1gyExkL0=" crossorigin="anonymous" />
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
    <!-- Bulma Version 0.6.2-->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.2/css/bulma.min.css" integrity="sha256-2k1KVsNPRXxZOsXQ8aqcZ9GOOwmJTMoOB5o5Qp1d6/s=" crossorigin="anonymous" />
    <style type="text/css">
        html,body {
            font-family: 'Open Sans';
        }
    </style>
</head>
<body>
<section class="hero is-fullheight is-default is-bold">
    <div class="hero-head">
        <nav class="navbar">
            <div class="container">
                <div class="navbar-brand">
                    <a class="navbar-item" href="../">
                        <img src="https://pbs.twimg.com/profile_images/1344530309/RLangTip_400x400.png" alt="Logo">
                    </a>
                    <span class="navbar-burger burger" data-target="navbarMenu">
            <span></span>
            <span></span>
            <span></span>
          </span>
                </div>
                <div id="navbarMenu" class="navbar-menu">
                    <div class="navbar-end"><div class="tabs is-right">

                    </div>
                    </div>
                </div>
            </div>
        </nav>
    </div>
    <div class="hero-body">
        <div class="container has-text-centered">

            <div class="column is-5 is-offset-3">
                <figure class="image is-4by3">
                    <img src="https://picsum.photos/800/600/?random" alt="Description">
                </figure>
            </div>
            <div class="column is-5 is-offset-3">

                <form class="form-request-help" method="POST" action="DispatcherServlet">
                    <h2 class="form-request-help-heading text-center">Roommates</h2>

                    <label for="username" class="sr-only">type your full name </label>
                    <input type="text" name="username" class="form-control" placeholder="Enter a username" required autofocus>

                    <label for="password" class="sr-only">type your full name </label>
                    <input type="text" name="password" class="form-control" placeholder="Enter your password" required autofocus>
                    <input type="hidden" name="command" value="userLogin">


                    <button class="btn btn-lg btn-primary btn-block" type="submit">submit</button>
                </form>

            </div>
        </div>
    </div>
    </div>

</section>
<script async type="text/javascript" src="../js/bulma.js"></script>
</body>
</html>