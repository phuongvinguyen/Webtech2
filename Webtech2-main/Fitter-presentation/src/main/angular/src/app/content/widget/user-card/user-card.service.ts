import { Injectable } from '@angular/core';
import {
  Post,
  PostResponse,
  UserProfile,
  UserService,
} from '../post/post.service';
import { UserSearchService } from '../user-search/user-search.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserCardService {
  users$: BehaviorSubject<UserProfile[]> = new BehaviorSubject<UserProfile[]>(
    []
  );

  constructor(private userSearchService: UserSearchService) {
    this.refreshUserList('');
  }
  searchForUsers(searchQuery: string){

  }

  refreshUserList(query: string) {
    this.userSearchService.searchUsers(query).subscribe({
      next: (response: UserProfile[]) => {
        this.users$.next(response);
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }
}
