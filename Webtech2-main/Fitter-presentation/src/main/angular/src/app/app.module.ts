import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DrawerComponent } from './drawer/drawer/drawer.component';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TimelineComponent } from './content/pages/timeline/timeline.component';
import { AdminTimelineComponent } from './content/pages/admin-timeline/admin-timeline.component';
import { Test1Component } from './content/pages/test1/test1.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AuthComponent } from './auth/auth/auth.component';
import { PostComponent } from './content/widget/post/post.component';
import { CreatePostComponent } from './content/widget/create-post/create-post.component';
import { ProfileComponent } from './content/pages/profile/profile.component';
import { TimlimePostComponent } from './content/widget/timlime-post/timlime-post.component';
import { AdminPostComponent } from './content/widget/admin-post/admin-post.component';
import { UserCardComponent } from './content/widget/user-card/user-card.component';
import { EmojiComponent } from '@ctrl/ngx-emoji-mart/ngx-emoji';
import { PickerComponent } from '@ctrl/ngx-emoji-mart';
import { RouterModule } from '@angular/router';
import { SettingsComponent } from './content/pages/settings/settings.component';
import { LeaderboardComponent } from './content/pages/leaderboard/leaderboard.component';
import { FormsModule } from '@angular/forms';
import { SearchBarComponent } from './content/widget/searchbar/searchbar.component';
import { UserSearchComponent } from './content/widget/user-search/user-search.component';
import { SearchTimelineComponent } from './content/pages/search/search.component';


@NgModule({
  declarations: [
    AppComponent,
    DrawerComponent,
    LoginComponent,
    SignupComponent,
    TimelineComponent,
    AdminTimelineComponent,
    Test1Component,
    AuthComponent,
    PostComponent,
    CreatePostComponent,
    ProfileComponent,
    PostComponent,
    TimlimePostComponent,
    AdminPostComponent,
    UserCardComponent,
    SettingsComponent,
    LeaderboardComponent,
    SearchBarComponent,
    UserSearchComponent,
    SearchTimelineComponent,
  
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    ReactiveFormsModule,
    HttpClientModule,
    EmojiComponent,
    PickerComponent,
    RouterModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}
