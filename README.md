# Sheriff-of-Nottingham
This is a implementation of the boardgame Sheriff of Nottingham in java. Here are 4 strategies and based on the cards ids, the players and theirs associations with a strategy given in input, this program will return each player with his points after the game.
The stregies:
  - basic: he is a fairplay player, allways puts in the sack the legal goods and when he is sheriff he inspects every sack
  - greedy: he plays similar with basic in odd rounds, but in even rounds he tries to add an illegal good in sack. At the inspection he checks everyone
  - bribed: he tries to put as much illegal goods as possible in his sack. When he is sheriff he inspects the player from his left side and the one from his right side
  - wizard: he sacrifices the first round to collect some informations about his opponents and then he plays based on this informations
  
  http://elf.cs.pub.ro/poo/teme/tema1
