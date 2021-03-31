<br />
<p align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">iPolitician</h3>

  <p align="center">
    by Free University of Tbilisi DSC
    <br />
    <a href="https://github.com/othneildrew/Best-README-Template">View Demo</a>
    ·
    <a href="https://github.com/othneildrew/Best-README-Template/issues">Report Bug</a>
    ·
    <a href="https://github.com/othneildrew/Best-README-Template/issues">Request Feature</a>
  </p>
</p>

<details open="open">
  <summary>Project Guide</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#the-goal">The Goal</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Usage</a>
      <ul>
        <li><a href="#prerequisites">Public Page</a></li>
        <li><a href="#prerequisites">Survey Page</a></li>
        <li><a href="#prerequisites">Profile Page</a></li>
        <li><a href="#prerequisites">Problems Page</a></li>
        <li><a href="#prerequisites">More App Features</a></li>
      </ul>
    </li>
    <li><a href="#faq">FAQ</a></li>
  </ol>
</details>

### About The Project

[![Product Name Screen Shot][product-screenshot]](https://example.com)

## The Goal

Nowadays, many people are not familiar with political parties and their opinion about the country's future. Also, due to the current situation with COVID-PANDEMIC, people aren't able to express their opinion in public, and we decided to create an application that describes the current political situation in the country.

## Built With
* [Android Studio](https://developer.android.com/studio?gclid=CjwKCAjwu5CDBhB9EiwA0w6sLWLJ3og1k0bkmBN5P4nD6h0r52Q7G6W0T94mRyCzvp3GO3yYhI1TZBoCEtIQAvD_BwE&gclsrc=aw.ds)
* [Firebase](https://firebase.google.com/?gclid=CjwKCAjwu5CDBhB9EiwA0w6sLXeffZ3yqVuWAcjTHE577SSDCVWdk4tW_OXXxJERXColzTOnBBs3OBoCWGkQAvD_BwE&gclsrc=aw.ds)


### Prerequisites

Download Android Studio - [Android Studio](https://developer.android.com/studio?gclid=CjwKCAjwu5CDBhB9EiwA0w6sLWLJ3og1k0bkmBN5P4nD6h0r52Q7G6W0T94mRyCzvp3GO3yYhI1TZBoCEtIQAvD_BwE&gclsrc=aw.ds)

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/DSC-Free-University-of-Tbilisi/iPolitician.git
   ```
2. Open Project in Android Studio
3. Wait until synching gradle and indexing files
4. Run Project on Emulator or on the device with usb tethering














### FAQ

Q: Please clearly describe the United Nations' Sustainable Development goal(s) AND target(s) you are looking to solve with technology. What inspired you to select these specific goal(s) AND target(s)? *

A: Goal 16: Peace, Justice & Strong Institutions - Goal 4: Quality Education.
At first, this idea came to us based on our country's current political situation. We realized that many people are not familiar with political parties and their opinion about the country's future. Also, due to the current situation with COVID-19, people aren't able to express their opinion in public and we decided to create an application that describes the current political situation in the country.


Q: How does your solution address the challenge you are looking to solve for? *

A: It's focused on making political life public and educate people in politics to have their independent opinion and their vote shouldn't be influenced by others. Political knowledge will guarantee stronger institutions in the country.


Q: What do you see as the future / next steps for your project? How will you expand your solution to reach a larger audience? *

A: We would like to involve as many political parties as possible which will be participating in our project. People will like it, because of its transparency, publicity and this will be the place where they can vote for any governmental issue and speak up anonymously. 


Q: Walk us through the steps you took to test your solution. Can you provide three specific feedback points you received from users that tested out your solution? *

A: Users gave us feedback about authorization where we must make sure that any person can vote at most once. We got some feedback about a survey which should be bigger and cover more subjects and make the person clear with the choice. Another feedback was about adding not only political parties but also candidates as independent political figures.


Q: Please share the outcome of your testing strategy. What are three specific things you implemented and improved for your solution based on the feedback from users? *

A: For the first feedback, we need to make authorization with people IDs which will give us stronger consistency of votes and the second issue is connected with parties which should support us with more question and their ideas. We would like to implement all ideas that were given to us based on our feedback.


Q: Describe the architecture that your team chose for your solution. What are the high-level components of your architecture? What is the responsibility of each component? Which specific products and platforms did you choose to implement these components? *

A: We chose Android Application because most people use Android devices nowadays and it's easier for us to have a large audience. The application uses simple mobile navigation where each page has a fragment component. Some fragments contain recycler views which are synched with Google Firebase.

Q: Technical components - Backend, Frontend, Technologies, Programming languages and Tools used.
Highlight one challenge you faced while building your code, including detail on how you addressed the issue and the technical decisions and implementations you had to make. *

A: We used Android Studio as IDE and the programming language is only Kotlin. As you know UI is displayed with XML. We chose Google Firebase as our database and to be precise Google FireStore where we save all the data which is needed for the application. We also used some open-source custom charts by adding a dependency in Gradle.


[Optional] Q: Do you have feedback for the Google products/platforms you used? Are there any features you would like to add for those technologies?

A: We'd like you to know that it was easy to use Firestore which has a very comfortable environment for developers. We would like you to add some basic chart components and a more comfortable recycler view component with many built-in features like swiping and etc.
