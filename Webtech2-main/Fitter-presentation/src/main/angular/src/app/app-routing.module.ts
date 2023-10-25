import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './auth/signup/signup.component';
import { LoginComponent } from './auth/login/login.component';
import { TimelineComponent } from './content/pages/timeline/timeline.component';
import { AdminTimelineComponent } from './content/pages/admin-timeline/admin-timeline.component';
import { DrawerComponent } from './drawer/drawer/drawer.component';
import { Test1Component } from './content/pages/test1/test1.component';
import { ProfileComponent } from './content/pages/profile/profile.component';
import { SettingsComponent } from './content/pages/settings/settings.component';
import { LeaderboardComponent } from './content/pages/leaderboard/leaderboard.component';
import { SearchTimelineComponent } from './content/pages/search/search.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'test1', component: Test1Component },
  { path: 'timeline', component: TimelineComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'leaderboard', component: LeaderboardComponent },
  { path: 'search', component: SearchTimelineComponent },
  { path: 'admintimeline', component: AdminTimelineComponent },
  { path: 'profile/:username', component: ProfileComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
