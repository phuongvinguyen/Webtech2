import { Component } from '@angular/core';
import { GlobalService } from './global.service';
import { UserProfile, UserService } from './content/widget/post/post.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
})
export class AppComponent {
  title = 'Angular';
  isLoggedIn?: boolean;
  user: UserProfile | null = null;

  constructor(
    private globalService: GlobalService,
    private userService: UserService
  ) {
    globalService.isLoggedIn.subscribe((loggedIn) => {
      this.isLoggedIn = loggedIn;
      console.log('app component: ' + this.isLoggedIn);
    });
  }

  ngOnInit() {
    this.loadDrawerInformation();
  }

  loadDrawerInformation() {
    this.userService.getMe().subscribe({
      next: (response: UserProfile) => {
        this.user = response;
        if (this.user.username != null) {
          this.isLoggedIn = true;
        }
      },
      error: () => {},
    });
  }
}
