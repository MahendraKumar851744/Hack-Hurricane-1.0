[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/_Beeh9_kbhQ/1.jpg)](https://www.youtube.com/watch?v=_Beeh9_kbhQ)

This is a project made in the Hakathon "Hack Hurricane 1.0" solely.

The Project aims to solves the problem of checking whether the content is original or AI generated where online plagarism checkers fail.

AI is the oil in tech field thats what i think, so our aim is to develop some AI tool to solve the problem of checking the originality of content which has become a major problem in edu field. So, our project aims to solves the problem of checking whether the content is original or AI generated where online plagarism checkers fail.

First lets discuss about the background of this thought, recently in our college i have observed students submitting the project reports which are completely generated with the help of chat gpt and bard, but our teachers test the reports with online plagarism checkers like turnit or other which fails to find the lack of originality and also students are using gpt's to write their assessments so our project aims to solves the problem of checking whether the content is original or AI generated where online plagarism checkers fail.

FLow: Scans the written text and converts it to digital text using Firebase ML kit and then the text is sent to our server which recieves the text, and our pre trained model( roberta base openai detector) responds with the result which will be handled in frontend.

Usecases: 
1.Users can Scan the written text and get the classification result.
2.Users can upload images and get the result.
3.Users can also enter the text manually and get the result.

Challenges Faced

Frontend:
1.The first problem which i faced when integrating firebase ml kit in android project is, it is getting conflicted with google play services library which ate lot of time and i solved by implementing old versions of the ml kit which solved the issue.

2. Dealing with the asynchronous nature of OCR with UI elements lead to lot and lot of crashes later i made them to run thread safe which is a mile stone

3.While Implementing Camerax library for scanning 

4.Confusion in selecting the network library volley or retrofit, later fixed with volley

5.The UI part is tedious and took lot of time.

Backend;
1.The first and most difficult part in the project is deploying the pre trained model in the django frame work as there are less online resources.

2.Next problem is creating API's without which app won't work, and i have created only one api which of post method.

3.Finally making all of the different parts work.
