import { Component } from '@angular/core';
import {
  Post,
  PostResponse,
  UserService,
  UserProfile,
} from '../post/post.service';
import { Observable } from 'rxjs';
import { UserCardService } from './user-card.service';
import { UserSearchService } from '../user-search/user-search.service';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.sass'],
})
export class UserCardComponent {
  users$: Observable<UserProfile[]>;
  user: UserProfile | null = null;
  following: boolean = false;
  name: string | null = '';
  isOwnProfile = false;

  constructor(
    private userlist: UserCardService,
    private userService: UserService,
    private userSearchService: UserSearchService
  ) {
    this.users$ = this.userlist.users$;
  }

  ngOnInit(): void {
    this.userSearchService.searchUsers('');
    console.log(this.userlist.users$);
  }

  follow() {
    if (!this.following) {
      this.userService.follow(this.name!);
      this.user!.followers++;
    } else {
      this.userService.unfollow(this.name!);
      this.user!.followers--;
    }

    this.following = !this.following;
  }
}
