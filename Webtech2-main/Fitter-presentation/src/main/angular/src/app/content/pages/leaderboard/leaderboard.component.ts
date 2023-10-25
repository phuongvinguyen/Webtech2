import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.sass'],
})
export class LeaderboardComponent {
  leaderboardData: any;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.getLeaderBoard();
  }

  getLeaderBoard() {
    const url =
      'http://ls5vs019.cs.tu-dortmund.de:9001/api/v1/statistics?limit=3';

    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      withCredentials: true,
    };

    this.http.get(url, options).subscribe({
      next: (response) => {
        this.leaderboardData = response;
        console.log(response);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        console.log('Completed');
      },
    });
  }
}
