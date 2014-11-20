Duo Run
=======

**NOTE: TO CHECK FOR COLLISION, USE**
```java
Intersect.overlapConvexPolygons();//!!!!
```
**Images for Balls: (Place images in \android\assets)**
http://teamkarbon.com/cloud/public.php?service=files&t=69d7aca788e27f04971fad1bd79a314c
**Image for helpbutton:**
http://teamkarbon.com/cloud/public.php?service=files&t=e529fe4665ded4c64cfb423db3f42948

That's all folks...

[![Build Status](https://travis-ci.org/TeamKarbonOfficial/GDXTest.svg?branch=master)](https://travis-ci.org/TeamKarbonOfficial/GDXTest)

TODO
=======
- Replace Ball with sprites
- Make the obstacles.  **done**
- Make them work.
    - Get polygons done **done**
    - Get them to render via filled triangles **done**
    - Get them to have the proper colours and collision checks **done**
    - Make them bigger
    - Make them spawn properly
    - Make them less laggy...
- Find out a way to check for collisions **done**
- Actually check for collisions **done**
- Make an endgame situation function
- Create levels *suggestions?*
- Create a level select screen
- Create a main menu screen
    - Create an interface. **done**
        - The user collides with the buttons using the respectively coloured balls to "click" the buttons.
        - Make the interface work **done**
    - GO! button **done. TODO: Move to level select screen of some sort :P**
    - Options button
    - About button
    - Exit button *?*
    - Gamer Services button _**??**_
    - Help button
        - Note: This button is a tapped button, and not activated like the other buttons as an "Obstacle".
                Because the user needs to know how to play the game. And since the interface IS the game,
                making the help button part of the interface is just... Use helpbutton.png for this one.
- Have some options/whatever
- Add Game Service
    - Add at least 5 achievements (Required by Playstore) **suggestions?**
    - Add Events??
    - Add leaderboards for all the gamemodes
- Test it on other devices too (to make sure the resolutions work and that the graphics aren't off-screen)
