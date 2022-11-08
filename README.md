# 7quizzes

7quizzes is a game of quiz where you need to show everyone who's the smartest ! Answer questions, learn new information and get acquainted with new people as the game now supports multiplayer !
This README is a guide on how to launch an application and some other details.

## Launch
 
First, as it is Maven project, build the jar by running the command below in the root directory.

```sh
mvn package
```

Then you could launch the application itself with two ways: through IDE (press green triangle button around main() method) or through console by running the command below (path to jar should be written without asterisks).

```sh
java -jar *path to jar*
```

## Things you can do

1. You can create a room through POST method on address below. You will be automatically added to the created room.
In request body, you should point "playerId" that creates a room and "roomName".
```
http://7quizzes.local/api/rooms
```
2. You can join a room through POST method on address below. You will be added to the room. In request body
you should point "playerId".
```
http://7quizzes.local/api/rooms/{roomId}/join
```
3. You can get status of the game you want through GET method on address below.
You will get such information as: current in-game question, number of questions passed, total number of questions
and status of the game (is it in process or idle).
```
http://7quizzes.local/api/rooms/{roomId}/game
```
4. You can start the game through POST method on address below.
The status will change to "in process" and you will be given a "questionId". In the request body, you should point
"playerId" (id of the player who starts a game).
```
http://7quizzes.local/api/rooms/{roomId}/game/start
```
5. Get question with answers list through GET method on address below.
```
http://7quizzes.local/api/rooms/{roomId}/game/question/{questionId}
```
6. Answer question through POST method on address below. Don't forget to point "answerId" and "playerId" in the request body.
You'll receive such information as: id of correct answer, how many points you got for this question, how many points you have overall.
Depending on the correctness of the answer, you'll receive either next question id (in case of correct answer or last answer) or current question id (in case of failed answer).
```
http://7quizzes.local/api/rooms/{roomId}/game/question/{questionId}/answer
```
7. You can get list of rooms through GET method on address below.
```
http://7quizzes.local/api/rooms
```
8. You can get certain room through GET method on address below.
```
http://7quizzes.local/api/rooms/{roomId}
```

## Things you can't do
1. Start the game if it's already in the process.
2. Start the game from outside the room.
3. Join the room while you're already inside that room.
4. Answer the same question twice.

## FAQ

1. <b>What happens if no one answers the question correctly ?</b>

<b>Answer:</b> After all the players send their answers, and they are all incorrect, then you'll receive next question id.

2. <b>What happens if someone answers the question correctly first ?</b>

<b>Answer:</b> He'll receive next question id and the question will change. If you'll try to answer the previous question, you'll get current question id.

3. <b>Can I be in a few rooms at the same time?</b>

<b>Answer:</b> Yes, of course !

4. <b>Can I join the game while it is in process?</b>

<b>Answer:</b> Yes, join everyone amidst the battle and start gaining points from the moment you have joined.

5.<b>What happens in case I answer the question incorrectly ?</b>

<b>Answer:</b> You'll have to wait until someone answers the question correctly or until all players have answered.

## To Be Continued ...
