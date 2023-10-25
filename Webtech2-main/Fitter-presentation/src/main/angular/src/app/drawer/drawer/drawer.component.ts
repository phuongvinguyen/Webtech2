import { HttpClient } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostComponent } from 'src/app/content/widget/post/post.component';
import {
  UserProfile,
  UserService,
} from 'src/app/content/widget/post/post.service';
import { GlobalService } from 'src/app/global.service';

@Component({
  selector: 'app-drawer',
  templateUrl: './drawer.component.html',
  styleUrls: ['./drawer.component.sass'],
})
export class DrawerComponent implements OnInit {
  i = -100;
  @Input() drawerUser?: UserProfile | null = null;

  constructor(
    private globalService: GlobalService,
    private router: Router,
    private userService: UserService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    console.log('Test');
  }

  showDiv(): boolean {
    return this.i % 2 === 0;
  }

  logout() {
    this.router.navigateByUrl('/login');
    this.globalService.logout();

    const url = `http://ls5vs019.cs.tu-dortmund.de:9001/logout`;
    this.http.post(url, { withCredentials: true }).subscribe({
      next: (response) => {
        console.log('Reaction added', response);
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }

  refreshPosts() {}
}
