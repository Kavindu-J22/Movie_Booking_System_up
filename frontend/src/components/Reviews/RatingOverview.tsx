import React from 'react';
import {
  Box,
  Typography,
  Rating,
  LinearProgress,
  Paper,
  Grid,
} from '@mui/material';
import { Star } from '@mui/icons-material';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';

interface RatingOverviewProps {
  movieId: string;
}

const RatingOverview: React.FC<RatingOverviewProps> = ({ movieId }) => {
  const { movieRating } = useSelector((state: RootState) => state.reviews);

  if (!movieRating || movieRating.totalReviews === 0) {
    return (
      <Paper sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6" gutterBottom>
          No ratings yet
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Be the first to rate this movie!
        </Typography>
      </Paper>
    );
  }

  const { averageRating, totalReviews, ratingDistribution } = movieRating;

  const getRatingPercentage = (rating: number) => {
    const count = ratingDistribution[rating - 1] || 0;
    return totalReviews > 0 ? (count / totalReviews) * 100 : 0;
  };

  return (
    <Paper sx={{ p: 3, mb: 3 }}>
      <Grid container spacing={3}>
        {/* Overall Rating */}
        <Grid item xs={12} md={4}>
          <Box textAlign="center">
            <Typography variant="h2" component="div" sx={{ fontWeight: 'bold', mb: 1 }}>
              {averageRating.toFixed(1)}
            </Typography>
            <Rating value={averageRating} readOnly precision={0.1} size="large" />
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              Based on {totalReviews} review{totalReviews !== 1 ? 's' : ''}
            </Typography>
          </Box>
        </Grid>

        {/* Rating Distribution */}
        <Grid item xs={12} md={8}>
          <Typography variant="h6" gutterBottom>
            Rating Breakdown
          </Typography>
          {[5, 4, 3, 2, 1].map((rating) => (
            <Box key={rating} display="flex" alignItems="center" mb={1}>
              <Box display="flex" alignItems="center" minWidth={60}>
                <Typography variant="body2" sx={{ mr: 1 }}>
                  {rating}
                </Typography>
                <Star sx={{ fontSize: 16, color: 'gold' }} />
              </Box>
              <Box flex={1} mx={2}>
                <LinearProgress
                  variant="determinate"
                  value={getRatingPercentage(rating)}
                  sx={{
                    height: 8,
                    borderRadius: 4,
                    backgroundColor: 'grey.200',
                    '& .MuiLinearProgress-bar': {
                      backgroundColor: 'gold',
                    },
                  }}
                />
              </Box>
              <Typography variant="body2" minWidth={40} textAlign="right">
                {ratingDistribution[rating - 1] || 0}
              </Typography>
            </Box>
          ))}
        </Grid>
      </Grid>
    </Paper>
  );
};

export default RatingOverview;
