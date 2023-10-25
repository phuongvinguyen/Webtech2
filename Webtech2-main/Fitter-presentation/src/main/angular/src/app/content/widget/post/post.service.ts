// user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppComponent } from 'src/app/app.component';

export interface Post {
  id: number;
  date: string;
  description: string;
  steps: number;
  username: string;
  showEmojiPicker: boolean;
  reactions: {
    alreadyReacted: boolean;
    amount: number;
    reactions: {
      [key: string]: number;
    };
  };
}

export interface PostResponse {
  postCount: number;
  posts: Post[];
  username: string;
}

export interface UserProfile {
  username: string;
  joined: string;
  followed: number;
  following: boolean;
  posts: number;
  followers: number;
  admin: boolean;
  totalSteps: number;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private API_URL = 'http://ls5vs019.cs.tu-dortmund.de:9001/api/v1/';

  constructor(private http: HttpClient) {}

  getPosts(username: string): Observable<PostResponse> {
    const url = `${this.API_URL}user/${username}/posts?limit=10`;
    return this.http.get<PostResponse>(url, { withCredentials: true });
  }

  updatePost(post: Post) {
    const url = `${this.API_URL}post/${post.id}`;
    const body = {
      steps: post.steps.toString(),
      description: post.description.toString(),
    };
    return this.http.put(url, body, { withCredentials: true });
  }

  getMe(): Observable<UserProfile> {
    const url = `${this.API_URL}user/me`;
    return this.http.get<UserProfile>(url, { withCredentials: true });
  }

  getProfileInformation(username: string): Observable<UserProfile> {
    const url = `${this.API_URL}user/${username}`;
    return this.http.get<UserProfile>(url, { withCredentials: true });
  }

  addReaktion(postID: number, reaction: string) {
    const url = `${this.API_URL}post/${postID}/reaction`;
    const params = new HttpParams().set('reaction', reaction);
    return this.http.post(url, params, { withCredentials: true });
  }

  removeReaktion(postID: number) {
    const url = `${this.API_URL}post/${postID}/reaction`;
    return this.http.delete(url, { withCredentials: true });
  }

  removePost(postID: number) {
    const url = `${this.API_URL}post/${postID}`;
    return this.http.delete(url, { withCredentials: true });
  }

  follow(followName: string) {
    const url = `${this.API_URL}user/${followName}/follow`;
    const options = {
      observe: 'response' as const,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      withCredentials: true,
    };
    this.http.post(url, {}, options).subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        // Code to be executed once the Observable completes
        console.log('Completed');
      },
    });
  }

  unfollow(followName: string) {
    const url = `${this.API_URL}user/${followName}/follow`;
    const options = {
      observe: 'response' as const,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      withCredentials: true,
    };
    this.http.delete(url, options).subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        // Code to be executed once the Observable completes
        console.log('Completed');
      },
    });
  }

  getTimelinePosts(): Observable<PostResponse> {
    const url = `${this.API_URL}timeline?limit=10`;
    return this.http.get<PostResponse>(url, { withCredentials: true });
  }

  getAllPosts(): Observable<PostResponse> {
    const url = `${this.API_URL}posts?limit=10`;
    return this.http.get<PostResponse>(url, { withCredentials: true });
  }
}
