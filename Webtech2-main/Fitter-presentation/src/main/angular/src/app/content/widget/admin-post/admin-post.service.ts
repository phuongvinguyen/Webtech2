import { Injectable } from '@angular/core';
import { Post, PostResponse, UserService } from '../post/post.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdminPostService {
  private _posts: BehaviorSubject<Post[]> = new BehaviorSubject<Post[]>([]);

  constructor(private userService: UserService) {
    this.refreshTimeline();
  }

  get posts(): Observable<Post[]> {
    return this._posts.asObservable();
  }

  refreshTimeline() {
    this.userService.getAllPosts().subscribe({
      next: (response: PostResponse) => {
        this._posts.next(response.posts);
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }
}
