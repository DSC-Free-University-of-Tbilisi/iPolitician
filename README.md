[![Contributors][contributors-shield]][contributors-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]


<br />
<p align="center">
  <a href="https://github.com/DSC-Free-University-of-Tbilisi/iPolitician">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">iPolitician</h3>

  <p align="center">
    by <br />
    DSContinental <br />
    Free University of Tbilisi <br />
    <img src="https://res.cloudinary.com/startup-grind/image/upload/dpr_2.0,fl_sanitize/v1/gcs/platform-data-dsc/contentbuilder/logo_hPnue3j.svg" class="svg" alt="Developer Student Clubs logo"> <br />
    <a href=youtube.com">View Demo</a>
    ·
    <a href="https://github.com/DSC-Free-University-of-Tbilisi/iPolitician/issues">Report Bug</a>
    ·
    <a href="https://github.com/DSC-Free-University-of-Tbilisi/iPolitician/issues">Request Feature</a>
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
      <a href="#current-and-next-steps">Current And Next Steps</a>
      <ul>
        <li><a href="#welcome">Welcome</a></li>
        <li><a href="#public-page">Public Page</a></li>
        <li><a href="#survey-page">Survey Page</a></li>
        <li><a href="#profile-page">Profile Page</a></li>
        <li><a href="#problems-page">Problems Page</a></li>
        <li><a href="#election-page">Election Page</a></li>
        <li><a href="#vocabulary-page">Vocabulary Page</a></li>
      </ul>
    </li>
    <li><a href="#faq">FAQ</a></li>
  </ol>
</details>

---
## About The Project

   Nowadays, many people are not familiar with political parties and their opinion about the country's future.
Also, due to the current situation with COVID19-PANDEMIC, people aren't able to express their opinion in public,
and we decided to create an application that describes the current political situation in the country.

  It's focused on making political life public and educate people in politics to have their independent opinion and their vote shouldn't be influenced by others. Political knowledge will guarantee stronger institutions in the country.

### The Goal
                
                                                          With this application:
- You can now gain some knowledge in political life, find the party which shares you opinion and world view.
- Decide on your own!
- You can take part in voting for any problems that consists in your country/region.
- Political life should be partially digitized so emigrants are able to influence the fate of motherland.
- More people are politically educated stronger countries institutions become.
- Create safe and trusted data by people.

### Built With
* [Android Studio](https://developer.android.com/studio?gclid=CjwKCAjwu5CDBhB9EiwA0w6sLWLJ3og1k0bkmBN5P4nD6h0r52Q7G6W0T94mRyCzvp3GO3yYhI1TZBoCEtIQAvD_BwE&gclsrc=aw.ds)
* [Firebase](https://firebase.google.com/?gclid=CjwKCAjwu5CDBhB9EiwA0w6sLXeffZ3yqVuWAcjTHE577SSDCVWdk4tW_OXXxJERXColzTOnBBs3OBoCWGkQAvD_BwE&gclsrc=aw.ds)
---

## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

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
---

## Current And Next Steps
Tips how the application now works and features should be done during next steps. 

### Welcome

- Current State

At the first launch of the application on the device, the User will be asked: "tell a little bit about yourself".

The user should select - the age range he/she/non-binary belongs.
The user should select - the gender he/she/non-binary belongs to.

- Next Step / App Feature

The user should select - the region he/she/non-binary lives in.
[optional] - the user should be asked to use location permission to make sure the region was selected correctly.

### Public Page

- Current State

The Display of Charts and Statistics of all users let the user find out how the public thinks nowadays and which political parties are in the lead.
Also, they can use filters to see how the different categories of people think and what’s their opinion.

- Next Step / App Feature

Add charts and statistic of all the other pages(currently, it contains only the survey page stats):

  from Profile Page - add region filter, citizen from (ქართლი/გურია/აჭარა/სამეგრელო/რაჭა/ხევსურეთი/სვანეთი/მთიულეთი/კახეთი/იმერეთი) and citizen from outside country (emigrant / ემიგრანტი).

  from Problems Page - add most important problems according to the votes.
  
  from Election Page - add which candidats are liked most by publc.


### Survey Page

- Current State

The user can answer the questions and then submit the survey, after this user will be able to see
which political parties are best matched and suitable to keep them up and vote in the election.
Also, the user is able to change the answers and resubmit the survey, but for every user, only the last submitted survey is counted.

- Next Step / App Feature

Negotiation with other political parties increases the number of questions in the survey, which will guarantee better results after taking the survey.

### Profile Page

- Current State

The user can update the personal info. Also, now the user is identified per-app-installation,
This means the user is unique by app installation - the unique id is generated in the first launch by app context.  

- Next Step / App Feature

* To avoid false/double/spam votes the user should be identified per-device by using the Mac Address of the Device.
* To make stronger consistency data, as we know lots of people have multiple devices today, the user should be identified by a real ID, but here comes another problem about data security of citizens.

### Problems Page

- Current State

The user can vote whether the problems should be solved at first by the government or not.

- Next Step / App Feature

The user will be able to post the problem and wait for how the public will react or search similar problems.
Also, the users should be given limited posts to avoid spam.

### Election Page

- Next Step / App Feature

This page should be like an online election in different aspects, like the whole country or given regions.
If today is the election who wins - the user should vote for the candidates.
Incase, by adding location future we can avoid votes from other regions.

### Vocabulary Page

- Next Step / App Feature

The user should be able to find the political word or phrases and find out what they mean
or ask the word/phrase he/she couldn't understand and wait for the answers. Here the users will be able to
help each other to become more politically educated.

---
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
---

## Acknowledgements 
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/DSC-Free-University-of-Tbilisi/iPolitician/graphs/contributors
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/DSC-Free-University-of-Tbilisi/iPolitician/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/DSC-Free-University-of-Tbilisi/iPolitician/issues
