import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserProfile, UserService } from '../../widget/post/post.service';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.sass'],
})
export class ProfileComponent {
  name: string | null = '';
  user: UserProfile | null = null;
  following: boolean = false;
  isOwnProfile = false;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private appComponent: AppComponent
  ) {}

  ngOnInit() {
    this.getProfileData();
  }

  getProfileData() {
    this.route.paramMap.subscribe((params) => {
      let username = params.get('username');
      this.name = username;

      this.userService.getProfileInformation(username!).subscribe({
        next: (response: UserProfile) => {
          this.user = response;
          this.isOwnProfile = this.appComponent.user?.username == username;
          this.following = this.user.following ?? false;
        },
        error: (error) => {
          console.error('There was an error!', error);
        },
      });
    });
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
